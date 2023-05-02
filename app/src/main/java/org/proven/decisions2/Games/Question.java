package org.proven.decisions2.Games;


import android.app.Activity;

import org.proven.decisions2.R;

public class Question extends Activity {

    public static int question[] ={
            // Geography
            R.string.geo1,
            R.string.geo2,
            R.string.geo3,
            R.string.geo4,
            R.string.geo5,
            R.string.geo6,
            R.string.geo7,
            R.string.geo8,
            R.string.geo9,
            // Science
            R.string.sci1,
            R.string.sci2,
            R.string.sci3,
            R.string.sci4,
            // Art and Culture
            R.string.art1,
            R.string.art2,
            R.string.art3,
            R.string.art4,
            R.string.art5,
            // Technology
            R.string.tec1,
            R.string.tec2,
            R.string.tec3,
            R.string.tec4,
            // Astrology
            R.string.ast1,
            R.string.ast2,
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
            2, 2, 1, 0, 2,
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



