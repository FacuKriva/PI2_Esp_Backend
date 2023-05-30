package com.digital.money.msvc.api.account.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Slf4j
@Component
public class KeysGenerator {

    public static String generateCvu() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10));
        }
        log.info("Cvu1: " + sb.toString());
        return sb.toString();
    }

    public static String generateAlias() {
        String pathFile = "msvc.api.account\\src\\main\\resources\\words.txt";
        //String pathFile = "./src/main/resources/words.txt";
        List<String> words = readFile(pathFile);
        Random random = new Random();
        int index1 = random.nextInt(words.size());
        int index2 = random.nextInt(words.size());
        while (index2 == index1) {
            index2 = random.nextInt(words.size());
        }
        int index3 = random.nextInt(words.size());
        while (index3 == index1 || index3 == index2) {
            index3 = random.nextInt(words.size());
        }
        String combination = words.get(index1).concat(".")
                .concat(words.get(index2))
                .concat(".")
                .concat(words.get(index3));
        log.info("Alias: " + combination);
        return combination;
    }

    public static List<String> readFile(String rutaArchivo) {
        List<String> words = new ArrayList<>();
        try {
            File archivo = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine()) {
                String palabra = scanner.nextLine();
                words.add(palabra);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find file " + rutaArchivo);
            e.printStackTrace();
        }
        return words;
    }
}
