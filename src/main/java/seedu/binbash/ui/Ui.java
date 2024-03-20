package seedu.binbash.ui;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Ui {
    private static final String NEWLINE = System.lineSeparator();
    private static final String LOGO = "  ____  _       ____            _" + NEWLINE +
            " | __ )(_)_ __ | __ )  __ _ ___| |__" + NEWLINE +
            " |  _ \\| | '_ \\|  _ \\ / _` / __| '_ \\" + NEWLINE +
            " | |_) | | | | | |_) | (_| \\__ \\ | | |" + NEWLINE +
            " |____/|_|_| |_|____/ \\__,_|___/_| |_|" + NEWLINE + NEWLINE;
    private static final String WELCOME_MESSAGE = "Welcome to BinBash!";
    private static final String GOODBYE_MESSAGE = "Bye!";
    private static final String LINE_DIVIDER = "-------------------------------------------------------------";
    private static final Logger UILOGGER = Logger.getLogger("BinBashUi");

    private final Scanner in;
    private Terminal userTerminal;
    private LineReader input;
    private boolean isUserActive;

    public Ui() {
        in = new Scanner(System.in);
        try {
            userTerminal = TerminalBuilder.terminal();
        } catch (IOException e) {
            // don't handle for now
        }
        isUserActive = true;
        input = LineReaderBuilder.builder()
            .terminal(userTerminal)
            .build();
    }

    public boolean isUserActive() {
        return isUserActive;
    }

    public void setUserAsInactive() {
        isUserActive = false;
    }

    public String readUserCommand() {
        assert isUserActive();
        String userInput = input.readLine("binbash> ");
        UILOGGER.setLevel(Level.WARNING);
        UILOGGER.log(Level.INFO, "received raw user input: " + userInput);
        return userInput;
    }

    public void greet() {
        talk(LOGO + WELCOME_MESSAGE);
    }

    public void farewell() {
        assert !isUserActive();
        talk(GOODBYE_MESSAGE);
    }

    public void talk(String line) {
        System.out.println(LINE_DIVIDER + NEWLINE + line + NEWLINE + LINE_DIVIDER);
    }
}
