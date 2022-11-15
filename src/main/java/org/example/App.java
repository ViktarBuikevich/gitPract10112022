package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static List<Book> parseFileWithBooks(String filename) throws IOException {
        List<Book> books = new ArrayList<>();
        Workbook workbook = null;
        //try {
        FileInputStream fileInputStream = new FileInputStream(filename);
        if (filename.toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fileInputStream);

        } else if (filename.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook(fileInputStream);
        }
        if (workbook == null) {
            return null;
        }
        Book readBook = null;

        // work with whole file
        Sheet sheet = workbook.getSheetAt(0);
        //get rows
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row row = rowIterator.next(); //пропускаем строку с шапкой таблицы
        int countContinue = 0;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Iterator<Cell> cellIterator = row.iterator();
            int j = 0;
            readBook = new Book();
            cellIterator.hasNext(); //пропускаем первую строку
            int id = 0;

            try {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (j == 0) {
                        id = ((int) cell.getNumericCellValue());
                    }
                    if (j == 1) {
                        readBook.setTitle(cell.getStringCellValue());
                    }
                    if (j == 2) {
                        readBook.setAuthors(cell.getStringCellValue());
                    }
                    if (j == 3) {
                        readBook.setAverage_rating((float) cell.getNumericCellValue());
                    }

                    if (j == 7) {
                        readBook.setNum_pages((int) cell.getNumericCellValue());
                    }
                    if (j == 10) {                            /*
                            String s="05.09.2013";
                            SimpleDateFormat format = new SimpleDateFormat();
                            format.applyPattern("dd.MM.yyyy");
                            Date docDate= format.parse(s);
                             */
                        String dateS = cell.getStringCellValue();
                        SimpleDateFormat format = new SimpleDateFormat();
                        format.applyPattern("M/dd/yyyy");
                        Date pubDate = format.parse(dateS);

                        //Date docDate= format.parse(s);
                        readBook.setPublication_date(pubDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }

                    j++;
                    if (j > 10) {
                        books.add(readBook);
                        break;
                    }
                }

            } catch (IllegalStateException | ParseException e) {

                System.out.println("" + id + ". \tПропускем запись: " + readBook.toString());
                countContinue++;
                j++;
                continue;
            }

        }
        System.out.println("Не cчитали записей: " + countContinue);

        //TODO
        workbook.close();

        // } catch (IOException e) {
        //    throw new RuntimeException(e);
        //} finally {
        //TODO Ошибка если хочу закрыть книгу в разделе финали
        //workbook.close(); //Ошибка
        //}
        return books;
    }

    public static void writeToWord(List<Book> books, String fileName) {

        XWPFDocument document = new XWPFDocument();
        document.getParagraphs();
        // первый параграф жирный курсив
        XWPFParagraph paragraph = null;
        for (Book el : books) {
            paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(el.toString());
            //run.addBreak();
        }

        //Записываем в файл
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            document.write(out);
            out.close();
            document.close();
            System.out.println("Word документ создан и сохранен успешно: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        List<Book> books = null;
        try {
            books = parseFileWithBooks("src/main/resources/books.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("считали записей: " + books.size());

//        1/ Выбрать книги с рейтингом от 4,5 до 5 - сохранить в отдельны docx файл

        Predicate<Book> lambda = (el ->el.getAverage_rating()>4.5 && el.getAverage_rating()<5);
        String fileN = "src/main/resources/List_books1.docx";
        SelectBook(books, lambda, fileN);

//        2/ Выбрать книги с рейтингом от 4,4 до 4 - сохранить в отдельны docx файл
        lambda = (el ->el.getAverage_rating()>4.0 && el.getAverage_rating()<4.5);
        fileN = "src/main/resources/List_books2.docx";
        SelectBook(books, lambda, fileN);

//        3/ Выбрать книги объемом от 0 до 100 стр - сохранить в отдельный docx файл

//        4/ Выбрать книги объемом от 101 до 101 стр - сохранить в отдельный docx файл
//        5/ Выбрать все книги выпущенные с 01/01/2005 до настоящего времени написанные на французком языке - сохранить в отдельный файл.


    }
    public static void SelectBook(List<Book> books, Predicate<Book> lambda, String fileN){
        List<Book> l1 = books.stream().filter(lambda).collect(Collectors.toList());
        //String fileN = "src/main/resources/List_books.docx";
        writeToWord(books, fileN);
        String file1 = "src/main/resources/List_books1.docx";
        writeToWord(l1, file1);
        System.out.println("Записали файл: "+fileN);

    }

}
//interface Expression{
//    boolean isEqual(Book n);
//}