/**
 * VideoScanner
 * 28.02.2012
 * @author Philipp Haussleiter
 *
 */
package jobs;

import helpers.SystemHelper;
import helpers.parser.SimpeOutputPP;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import models.Tag;
import models.Video;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.vfs.VirtualFile;

public class VideoScanner extends Job {

    // TODO Replace with settings from application.conf
    private final static String USER = "philipp";
    private final static String HOST = "localhost";
    private static SystemHelper HELPER = new SystemHelper();
    private static VirtualFile INPUT;
    private static VirtualFile STORAGE;
    private final static String FFMPEG_CMD = "/usr/local/bin/ffmpeg";

    public VideoScanner() {
        VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
        INPUT = appRoot.child(Play.configuration.getProperty("video.upload"));
        STORAGE = appRoot.child(Play.configuration.getProperty("video.storage"));
    }

    @Override
    public void doJob() {
        Logger.info("start Scanning video folder");
        File file;
        Video video;
        Tag tag;
        for (VirtualFile v : INPUT.list()) {
            if (!v.getName().startsWith(".")) {
                file = v.getRealFile();
                video = Video.findOrCreateByFilename(file.getName().trim());
                for (String p : getNameTags(file.getName())) {
                    if (!p.isEmpty()) {
                        tag = Tag.findOrCreateByName(p.toLowerCase().trim());
                        video.addTag(tag);
                    }
                }
                for (String p : getVideoTags(file.getAbsolutePath())) {
                    if (!p.isEmpty()) {
                        tag = Tag.findOrCreateByName(p.toLowerCase().trim());
                        video.addTag(tag);
                    }
                }
                video.save();
            }
        }
        Logger.info("done Scanning video folder");
    }

    private String[] getNameTags(String text) {
        try {
            text = URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.warn(ex.getLocalizedMessage());
        }
        text = text.toLowerCase().replace("%", " ");
        String[] findList = {" ", "#", "%", ":", "=", "\n", "_", "?", ".", ",", "!", "/", "\\", "\"", "„", "“", "(", ")", "[", "]", "{", "}", "<", ">"};
        for (String find : findList) {
            text = text.replace(find, "-");
        }
        Logger.info("getNameTags: cleaned name: " + text);
        return text.split("-");
    }

    private String[] getVideoTags(String filename) {
        String cmd = FFMPEG_CMD + " -i " + filename + " 2>&1 ";
        String videoTags = "";
        SimpeOutputPP sop = new SimpeOutputPP();
        HELPER.runCommand(USER, HOST, cmd, sop);
        for (String line : sop.getOutput()) {
            if (line.startsWith("Stream")) {
                videoTags += line.replace("Stream", "") + " ";
            }
        }
        return getNameTags(videoTags);
    }
}
