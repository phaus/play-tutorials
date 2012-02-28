/**
 * VideoScanner
 * 28.02.2012
 * @author Philipp Haussleiter
 *
 */
package jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import models.Tag;
import models.Video;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.vfs.VirtualFile;

public class VideoScanner extends Job {

    private static VirtualFile INPUT;
    private static VirtualFile STORAGE;
    private final static String FFMPEG_CMD = "/usr/local/bin/ffmpeg";
    private static String[] cmds = {
        "/bin/bash",
        "-c",
        ""
    };
    private Runtime r;

    public VideoScanner() {
        r = Runtime.getRuntime();
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
                    tag = Tag.findOrCreateByName(p.toLowerCase().trim());
                    video.addTag(tag);
                }
                getVideoTags(file.getAbsolutePath());
                video.save();
            }
        }
        Logger.info("done Scanning video folder");
    }

    private String[] getNameTags(String filename) {
        String find[] = {"-", " ", "_", ".", "?", "(", ")", "[", "]", "|"};
        for (String f : find) {
            filename = filename.replace(f, "-");
        }
        Logger.info("getNameTags: cleaned name: " + filename);
        return filename.split("-");
    }

    private void getVideoTags(String filename) {
        //String name = INPUT.getRealFile().getAbsolutePath() + "/" + filename;
        String cmd = FFMPEG_CMD + " -i " + filename;
        Logger.info("cmd: " + cmd);
        Logger.info("result:\n" + getCallResult(cmd) + "\n");
    }

    private String getCallResult(String cmd) {
        //"cmd /C dir"
        StringBuilder sb = new StringBuilder();
        try {
            cmds[2] = '"'+cmd+'"';
            Process p = r.exec(cmd);
            Logger.info("running");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }
}
