package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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

		while (!out) {
			System.out.println("SCHOOL PERSONNEL AND STUDENT MANAGEMENT:");
			System.out.println("Escriba 'help' para mostrar la ayuda o 'exit' para cerrar la aplicacion.");
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
				break;
			case "assign":
				MainMethods.assignStudent();
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
				break;
			case "exit":
				out = true;
				break;
			default:
				System.out.println("Ese comando no se reconoce, intentelo de nuevo. Si necesita ayuda, escriba help para mostrarla\n");

			}

		}
//		 TeachersHandler th = new TeachersHandler(db.getCollection("teachers"));
//		 th.insertTeacher();
//		 th.selectTeacher();

//		 
//		 SubjectHandler sh = new SubjectHandler(db.getCollection("subjects"));
//		 sh.insertSubject();

//		 sh.selectSubject();

	}

}
