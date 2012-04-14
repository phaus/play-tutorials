/**
 * BooleanParser
 * 29.03.2012
 * @author Philipp Haussleiter
 *
 */
package helpers.parser;

import helpers.ProcessParser;
import java.io.BufferedReader;
import java.io.IOException;
import play.Logger;

public class BooleanPP implements ProcessParser {

    private boolean result = true;

    public void parse(BufferedReader bufferedreader) {
        try {
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                if (line.trim().equals("0")) {
                    Logger.info("no PW needed!");
                    result = false;
                }
            }
        } catch (IOException ex) {
            Logger.error(ex.getLocalizedMessage());
        }
    }

    public boolean getResult() {
        return result;
    }
}
