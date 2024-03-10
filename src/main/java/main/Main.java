package main;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

	static MongoClient mc;
	static MongoDatabase db;
	static TeacherHandler th;
	static SubjectHandler sh;
	static StudentHandler ah;
	static MongoCollection<Document> teachers;
	static MongoCollection<Document> students;
	static MongoCollection<Document> subjects;

	public static void main(String[] args) {

		MainMethods.initialize();
		boolean out = false;
		String command;
		Scanner scanner = new Scanner(System.in);

		// TODO: Comprobar que todo esta bien definido (deberia)
		while (!out) {
			System.out.println("SCHOOL PERSONNEL AND STUDENT MANAGEMENT:");
			System.out.println("Write 'help' if needed or 'exit' to close the application.");
			System.out.print(">");
			command = scanner.nextLine();

			switch (command.substring(0, command.indexOf(' ') == -1 ? command.length() : command.indexOf(' '))) {
			case "help":
				MainMethods.help();
				break;
			case "sel":
				if (command.contains("--a")) {
					ah.selectStudent();
					break;
				}
				if (command.contains("--t")) {
					th.selectTeacher();
					break;
				}
				if (command.contains("--s")) {
					sh.selectSubject();
					break;
				}
				if (command.contains("--rel-ts")) {
					MainMethods.findCommonSubjects();
					break;
				}
				if (command.contains("--rel-mg")) {
					th.selectManagers();
					break;
				}
				System.err.println("Wrong command. Please, try again.");
				break;
			case "new":
				if (command.contains("--a")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						ah.insertStudent(Integer.parseInt(command));
						break;
					}
					ah.insertStudent();
					break;
				}
				if (command.contains("--t")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						th.insertTeacher(Integer.parseInt(command));
						break;
					}
					th.insertTeacher();
					break;
				}
				if (command.contains("--s")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						sh.insertSubject(Integer.parseInt(command));
						break;
					}
					sh.insertSubject();
					break;
				}
				System.err.println("Wrong command. Please, try again.");
				break;
			case "add":
				if (command.contains("--st")) {
					MainMethods.insertSubjectIntoTeacher();
					break;
				}
				if (command.contains("--sa")) {
					MainMethods.insertSubjectIntoStudent();
					break;
				}
				System.err.println("Wrong command. Please, try again.");
				break;
			case "sp":
				if (command.contains("--a ")) {
					command = command.substring(command.indexOf("--a ")+"--a ".length());
					ah.selectStudent(command);
					break;
				}
				if (command.contains("--t ")) {
					command = command.substring(command.indexOf("--t ")+"--t ".length());
					th.selectTeacher(command);
					break;
				}
				if (command.contains("--s ")) {
					command = command.substring(command.indexOf("--s ")+"--s ".length());
					sh.selectSubject(command);
					break;
				}
				System.err.println("Wrong command. Please, try again.");
				break;
			case "update":
				if (command.contains("--a ")) {
					ah.updateStudent();
					break;
				}
				if (command.contains("--t ")) {
					th.updateTeacher();
					break;
				}
				if (command.contains("--s ")) {
					sh.updateSubject();
					break;
				}
				break;
			case "assign":
				MainMethods.assignStudent();
				break;
			case "fields":
				MainMethods.showFields();
				break;
			case "exit":
				out = true;
				break;
			default:
				System.err.println("Unknown command, try again. If help is needed, write help to show.\n");
			}
		}
		scanner.close();
		System.exit(0);
	}
}
