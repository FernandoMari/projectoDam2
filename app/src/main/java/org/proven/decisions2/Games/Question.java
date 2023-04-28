package org.proven.decisions2.Games;


import android.app.Activity;

import org.proven.decisions2.R;

import java.util.Locale;

public class Question extends Activity {

    public static String question[] ={
            // Geography
            "What is the largest continent in the world by area?",
            "What is the smallest country in the world?",
            "What is the highest mountain in the world?",
            "What is the largest country in South America?",
            "What is the capital of Egypt?",
            "What is the currency of Brazil?",
            "What is the capital of Australia?",
            "Which city is the capital of Japan?",
            "Which river is the longest in the world?",
            // Science
            "Which famous physicist developed the theory of relativity?",
            "What is the largest organ in the human body?",
            "What is the chemical symbol for gold?",
            "Who discovered penicillin?",
            // Art and Culture
            "Which famous artist painted the Mona Lisa?",
            "Who painted the famous work 'Starry Night'?",
            "Who wrote the novel 'To Kill a Mockingbird'?",
            "Who wrote the play 'Romeo and Juliet'?",
            "Who directed the movie 'Jaws'?",
            // Technology
            "Which company owns the Android?",
            "Which company owns the Apple?",
            "What app was founded in 2005?",
            "Which one is not a programming language?",
            // Astrology
            "Which planet in our solar system is the largest?",
            "What is the smallest planet in our solar system?",
    };

    public static String answers[][] = {
            // Geography
            {"South America", "Asia", "North America", "Africa"},
            {"Tuvalu", "Nauru", "Vatican City", "Monaco"},
            {"Kangchenjunga", "K2", "Mount Everest", "Lhotse"},
            {"Peru", "Colombia", "Argentina", "Brazil"},
            {"Cairo", "Giza", "Alexandria", "Luxor"},
            {"Peso", "Euro", "Dollar", "Real"},
            {"Melbourne", "Sydney", "Brisbane", "Canberra"},
            {"Kyoto", "Tokyo", "Nagoya", "Osaka"},
            {"Amazon", "Nile", "Mississippi", "Yangtze"},
            // Science
            {"Stephen Hawking", "Galileo Galilei", "Isaac Newton", "Albert Einstein"},
            {"Brain", "Heart", "Skin", "Liver"},
            {"Au", "Cu", "Fe", "Ag"},
            {"Louis Pasteur", "Marie Curie", "Charles Darwin", "Alexander Fleming"},
            // Art and Culture
            {"Claude Monet", "Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh"},
            {"Pablo Picasso", "Leonardo da Vinci", "Vincent van Gogh", "Salvador Dali"},
            {"Ernest Hemingway", "Harper Lee", "Mark Twain", "J.D. Salinger"},
            {"William Shakespeare", "Oscar Wilde", "George Bernard Shaw", "Samuel Beckett"},
            {"George Lucas", "Martin Scorsese", "Steven Spielberg", "Francis Ford Coppola"},
            // Technology
            {"Nokia", "Samsung", "Google", "Apple"},
            {"Samsung", "Google", "Apple", "Nokia"},
            {"Instagram", "Whatsapp", "Facebook", "Youtube"},
            {"Java", "Python", "Kotlin", "Notepad"},
            // Astrology
            {"Mars", "Saturn", "Jupiter", "Uranus"},
            {"Neptune", "Mercury", "Venus", "Mars"}
    };

    public static int correctAnswer[] = {
            // Geography
            1, 2, 2, 3, 0, 3, 3, 1, 1,
            // Science
            3, 2, 0, 1,
            // Art and Culture
            2, 0, 2, 0, 2,
            // Technology
            2, 2, 3, 3,
            // Astrology
            2, 1
    };

    public static int category[] = {
            R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography,
            R.string.science, R.string.science, R.string.science, R.string.science,
            R.string.art, R.string.art, R.string.art, R.string.art, R.string.art,
            R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno,
            R.string.astro, R.string.astro
    };

}



