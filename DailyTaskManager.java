import java.util.Scanner;

public class DailyTaskManager{

    // Stack for Task Completion
    static class tasksStack {
        private String[] stack;
        private int top;
        private int capacity;

        public tasksStack(int size) {
            this.capacity = size;
            this.stack = new String[size];
            this.top = -1;
        }

        public void push(String task) {
            if (top == capacity - 1) {
                System.out.println("Tidak dapat menambah task yang sudah selesai");
            } else {
                stack[++top] = task;
                System.out.println("Task '" + task + "' sudah selesai");
            }
        }

        public String pop() {
            if (top == -1) {
                System.out.println("Tidak ada task untuk di undo");
                return null;
            } else {
                String undoneTask = stack[top--];
                System.out.println("Undo: Task '" + undoneTask + "' belum selesai");
                return undoneTask;
            }
        }

        public String peek() {
            if (top == -1) {
                return "belum ada task yang selesai";
            }
            return stack[top];
        }
    }

    // Linked List for managing tasks
    static class TaskLinkedList {
        class Node {
            String task;
            Node next;
            boolean isCompleted;
            boolean isNewlyAdded; // New flag to identify newly added tasks
            boolean isInitialTask; // Flag to identify if the task is from task2 (initial task)

            public Node(String task, boolean isInitialTask) {
                this.task = task;
                this.next = null;
                this.isCompleted = false;  // Initially, task is not completed
                this.isNewlyAdded = false;  // Task is not newly added by default
                this.isInitialTask = isInitialTask;  // Set if task is from task2
            }
        }

        private Node head;
        private Node tail;
        private Node lastCompletedTask;  // To store the most recent completed task
        private String[] newtask;  // Array to store newly added tasks

        public TaskLinkedList() {
            head = null;
            tail = null;
            lastCompletedTask = null;  // No task is completed initially
            this.newtask = new String[0];  // Initialize newtask array with size 0
        }

        // Add task to the end of the list and also to the newtask array
        public void addTask(String task) {
            Node newNode = new Node(task, false); // New task is not initial
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            newNode.isNewlyAdded = true;  // Mark this task as newly added
            System.out.println("Task " + task + "ditambah ke link list");
            
            // Add the new task to the newtask array
            addToNewTaskArray(task);
        }

        // Method to add task to the newtask array
        private void addToNewTaskArray(String task) {
            // Create a new array with an additional space
            String[] newArray = new String[newtask.length + 1];
            
            // Copy the old elements to the new array
            System.arraycopy(newtask, 0, newArray, 0, newtask.length);
            
            // Add the new task
            newArray[newtask.length] = task;
            
            // Update the newtask reference
            newtask = newArray;
        }

        // Remove task by index
        public void removeTask(int index) {
            if (head == null) {
                System.out.println("tidak ada task tersedia");
                return;
            }

            Node current = head;
            Node previous = null;
            int currentIndex = 0;

            // If the task to remove is at the head
            if (index == 0) {
                head = head.next;
                System.out.println("Task " + current.task + "dihilangkan");
                return;
            }

            // Traverse to the task at the given index
            while (current != null && currentIndex < index) {
                previous = current;
                current = current.next;
                currentIndex++;
            }

            if (current == null) {
                System.out.println("task dengan index " + index + " tidak ketemu");
            } else {
                previous.next = current.next;
                System.out.println("task " + current.task + "dihilangkan");
            }
        }

        // Mark task as completed by index
        public void markAsCompleted(int index, tasksStack taskStack) {
            Node current = head;
            int currentIndex = 0;

            // Traverse to the task at the given index
            while (current != null && currentIndex < index) {
                current = current.next;
                currentIndex++;
            }

            if (current == null) {
                System.out.println("task dengan indek " + index + " tidak ketemu");
            } else {
                current.isCompleted = true;
                taskStack.push(current.task);  // Push completed task to stack
                lastCompletedTask = current;  // Store the last completed task
                System.out.println("Task :" + current.task + " telah selesai");
            }
        }

        // Undo the most recently completed task
        public void undoLastCompletedTask(tasksStack taskStack) {
            if (lastCompletedTask != null) {
                lastCompletedTask.isCompleted = false;
                System.out.println("Task :" + lastCompletedTask.task + " telah di undo");
                taskStack.pop();  // Remove from the stack
                lastCompletedTask = null;  // Reset the last completed task after undo
            } else {
                System.out.println("tidak ada task yang bisa di undo ");
            }
        }

        // View the most recently completed task
        public void viewLastCompletedTask() {
            if (lastCompletedTask != null) {
                System.out.println("task terakhir yang selesai:" + lastCompletedTask.task + "'");
            } else {
                System.out.println("belum ada task yang selesai ");
            }
        }

        // Display all tasks with their index and status
        public void displayTasks() {
            if (head == null) {
                System.out.println("No tasks available.");
                return;
            }

            Node current = head;
            System.out.println("Task List (Linked List):");
            int index = 0;
            while (current != null) {
                String status = current.isCompleted ? "(selesai)" : "(belum selesai)";
                System.out.println(index+1 + ". " + current.task + " " + status);
                current = current.next;
                index++;  // Increment task number
            }
        }

        // Display only newly added tasks (Excluding initial tasks from task2)
        public void displayNewlyAddedTasks() {
            if (newtask.length == 0) {
                System.out.println("belum ada task tambahan baru ");
                return;
            }

            System.out.println("task tambahan baru :");
            for (int i = 0; i < newtask.length; i++) {
                System.out.println(i + 1 + ". " + newtask[i]);
            }
        }

        // Initialize the linked list with tasks from an array (task2)
        public void initializeWithArray(String[] tasks) {
            // Clear the newtask array so that it doesn't include tasks from task2
            newtask = new String[0]; // Clear any existing tasks in newtask
            for (String task : tasks) {
                addTask(task);  // Add each task from task2
            }
        }

        public String[] getNewTasks() {
            return newtask;
        }
    }

    public static void main(String[] args) {
        Scanner jawaban = new Scanner(System.in);

        // Initialize the stack
        tasksStack taskStack = new tasksStack(5);

        // Array of tasks for case 1 (static task array)
        String[] tasks = {
            "melihat email",
            "mengecek kewarasan",
            "melihat apakah batin baik baik saja",
            "menyentuh rumput",
            "tidur"
        };

        // Array of tasks for task2 (linked list management)
        String[] task2 = {
            "melihat email",
            "mengecek kewarasan",
            "melihat apakah batin baik baik saja",
            "menyentuh rumput",
            "ayam"
        };

        boolean[] status = new boolean[tasks.length];
        int pilihan;
        int nomor;
        int keluar;


        // Linked List to manage tasks
        TaskLinkedList taskLinkedList = new TaskLinkedList();

        // Initialize the Linked List with tasks from task2
        taskLinkedList.initializeWithArray(task2);

        while (true) {
            System.out.println("Pilih apakah anda ingin menggunakan:");
            System.out.println("1. Array");
            System.out.println("2. Linked List");
            System.out.println("3. Keluar");
            System.out.print("Masukkan pilihan anda: ");
            pilihan = jawaban.nextInt();

            switch (pilihan) {
                case 1:
                 // Task Manager using Array (Stack part)
                 while (true) {
                    System.out.println("\nSelamat datang di task manager (array)");
                    System.out.println("Apa yang ingin anda lakukan:");
                    System.out.println("1. Melihat semua task");
                    System.out.println("2. Menandai task yang sudah selesai");
                    System.out.println("3. Undo task yang sudah selesai");
                    System.out.println("4. Lihat task paling terakhir selesai");
                    System.out.println("5. Keluar dari task manager");
                    System.out.print("Silahkan masukkan pilihan anda: ");
                    pilihan = jawaban.nextInt();

                    switch (pilihan) {
                        case 1:
                            for (int i = 0; i < tasks.length; i++) {
                                if (status[i]) {
                                    System.out.println((i + 1) + ". " + tasks[i] + "(selesai)");
                                } else {
                                    System.out.println((i + 1) + ". " + tasks[i]);
                                }
                            }
                            break;

                        case 2:
                            System.out.print("Masukkan nomor dari task yang ingin anda selesaikan: ");
                            nomor = jawaban.nextInt();
                            if (nomor >= 1 && nomor <= tasks.length) {
                                status[nomor - 1] = true;
                                taskStack.push(tasks[nomor - 1]);
                            } else {
                                System.out.println("Nomor untuk yang dimasukkan tidak ada");
                            }
                            break;

                        case 3:
                            String undoneTask = taskStack.pop();
                            if (undoneTask != null) {
                                // Logic to undo (if needed)
                            }
                            break;

                        case 4:
                            System.out.println("Task yang terakhir diselesaikan: " + taskStack.peek());
                            break;

                        case 5:
                            System.out.println("Keluar dari task manager (array): ");
                            System.out.println("1. Iya");
                            System.out.println("2. Tidak");
                            System.out.print("Masukkan jawaban anda: ");
                            keluar = jawaban.nextInt();
                            if (keluar == 2) {
                                break;
                            }
                            if (keluar == 1) {
                                // Return to main menu after exiting
                                return;
                            }
                            break;

                        default:
                            System.out.println("Maaf, opsi ini tidak tersedia. Mohon pilih ulang.");
                    }
                }

                case 2:
                    // Task Manager using Linked List
                    while (true) {
                        System.out.println("\nSelamat datang di task manager (linked list)");
                        System.out.println("Apa yang ingin anda lakukan:");
                        System.out.println("1. Melihat semua task");
                        System.out.println("2. Menandai task yang sudah selesai");
                        System.out.println("3. Undo task yang sudah selesai");
                        System.out.println("4. Lihat task paling terakhir selesai");
                        System.out.println("5. Masukkan task yang ingin dihapus");
                        System.out.println("6. Masukkan task yang ingin ditambahkan");
                        System.out.println("7. Lihat hanya task yang baru ditambahkan");
                        System.out.println("8. Keluar dari task manager");
                        System.out.print("Silahkan masukkan pilihan anda: ");
                        int pilihan2 = jawaban.nextInt();
                        jawaban.nextLine();  // Consume newline character

                        switch (pilihan2) {
                            case 1:
                                taskLinkedList.displayTasks();
                                break;
                            case 2:
                                System.out.print("Masukkan nomor dari task yang ingin anda selesaikan: ");
                                nomor = jawaban.nextInt();
                                taskLinkedList.markAsCompleted(nomor - 1, taskStack);
                                break;
                            case 3:
                                taskLinkedList.undoLastCompletedTask(taskStack);
                                break;
                            case 4:
                                taskLinkedList.viewLastCompletedTask();
                                break;
                            case 5:
                                System.out.print("Masukkan nomor task yang ingin dihapus: ");
                                int nomorHapus = jawaban.nextInt();
                                taskLinkedList.removeTask(nomorHapus - 1);
                                break;
                            case 6:
                                System.out.print("Masukkan task yang ingin ditambahkan: ");
                                String taskBaru = jawaban.nextLine();
                                taskLinkedList.addTask(taskBaru);
                                break;
                            case 7:
                                taskLinkedList.displayNewlyAddedTasks();  // Display tasks from the newtask array
                                break;
                            case 8:
                                System.out.println("Terimakasih, keluar dari Task Manager.");
                                return;  // Exit back to main menu
                            default:
                                System.out.println("Pilihan tidak valid.");
                        }
                    }

                case 3:
                    System.out.println("Terimakasih telah menggunakan aplikasi!");
                    jawaban.close(); // Close the Scanner resource to avoid resource leak
                    return;  // Exit the program

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
