import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    public static void parseUserInput(String userInput, TaskList taskList, Storage storage) {
        if (userInput.equals("bye")) {
            // If user wants to quit
            storage.saveTasks(taskList); // Save changes to list
            Ui.showExit();
            return;

        } else if (userInput.equals("list")) {
            // If user wants to check list
            Ui.showList(taskList);

        } else if (userInput.startsWith("mark")) {
            // If user wants to mark something
            String[] parts = userInput.split(" ");
            if (parts.length == 2) {
                try {
                    int index = Integer.parseInt(parts[1]) - 1;
                    taskList.markTask(taskList.getTask(index));
                } catch (NumberFormatException e) {
                    Ui.showError("Invalid task number");
                }
            } else {
                // Invalid command format
                Ui.showError("Invalid command format");
            }

        } else if (userInput.startsWith("unmark")) {
            // If user wants to unmark something
            String[] parts = userInput.split(" ");
            if (parts.length == 2) {
                try {
                    int index = Integer.parseInt(parts[1]) - 1;
                    taskList.unmarkTask(taskList.getTask(index));
                } catch (NumberFormatException e) {
                    Ui.showError("Invalid task number");
                }
            } else {
                // Invalid command format
                Ui.showError("Invalid command format");
            }

        } else if (userInput.startsWith("todo") || userInput.startsWith("deadline") || userInput.startsWith("event")) {
            // User creates a task
            Task newTask = null;

            if (userInput.startsWith("todo")) {
                // If user wants to create a ToDos task
                if (userInput.length() < 5) {
                    // Incorrect input format for todos
                    Ui.showError("Incorrect input format for todo");
                } else {
                    String description = userInput.substring(5).trim();
                    if (description.isBlank()) {
                        // If there's do description
                        Ui.showError("The description of a todo cannot be empty");
                    } else {
                        newTask = new ToDos(description);
                        taskList.addTask(newTask);
                    }
                }

            } else if (userInput.startsWith("deadline")) {
                // If user wants to create a Deadlines task
                String[] parts = userInput.split("/by");

                if (parts.length == 2) {
                    // Makes sure deadline format is followed (e.g. there's /by)
                    String description = parts[0].substring(9).trim();
                    String by = parts[1].trim();

                    // Parse the date and time
                    LocalDate deadlineDate = LocalDate.parse(by);

                    if (description.isBlank() || by.isBlank()) {
                        // If description or by is empty
                        Ui.showError("The description or by of a deadline cannot be empty");
                    } else {
                        newTask = new Deadlines(description, deadlineDate);
                        taskList.addTask(newTask);
                    }
                } else {
                    // Incorrect input format for deadline
                    Ui.showError("Incorrect input format for deadline");
                }

            } else {
                // If user wants to create an Events task
                String[] parts = userInput.split("/from|/to");

                if (parts.length == 3) {
                    // Makes sure deadline format is followed (e.g. there's /from and /to)
                    String description = parts[0].substring(6).trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();

                    if (description.isBlank() || from.isBlank() || to.isBlank()) {
                        // If description, from, or to is empty
                        Ui.showError("The description or from or to of an event cannot be empty");
                    } else {
                        newTask = new Events(description, from, to);
                        taskList.addTask(newTask);
                    }
                } else {
                    // Incorrect input format for events
                    Ui.showError("Incorrect input format for event");
                }
            }

            if (newTask != null) {
                // If task is initialized
                Ui.showAddedTask(newTask, taskList);
            }

        } else if (userInput.startsWith("delete")) {
            // If user wants to delete a task
            String[] parts = userInput.split(" ");
            if (parts.length == 2) {
                try {
                    int index = Integer.parseInt(parts[1]) - 1;
                    taskList.deleteTask(taskList.getTask(index));
                } catch (NumberFormatException e) {
                    Ui.showError("Invalid task number");
                }
            } else {
                // Invalid command format
                Ui.showError("Invalid command format");
            }
        } else {
            // Other inputs
            Ui.showError("I'm sorry, but I don't know what that means");
        }
    }
}
