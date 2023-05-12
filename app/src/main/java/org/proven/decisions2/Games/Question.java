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
            R.string.sci5,
            R.string.sci6,
            R.string.sci7,
            R.string.sci8,
            R.string.sci9,
            R.string.sci10,
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
            R.string.tec5,
            R.string.tec6,
            // Astrology
            R.string.ast1,
            R.string.ast2,
            R.string.ast3,
            R.string.ast4,
            // History
            R.string.htr1,
            R.string.htr2,
            R.string.htr3,
            R.string.htr4,
            R.string.htr5,
            R.string.htr6,
            // Sport
            R.string.spt1,
            R.string.spt2,
            R.string.spt3,
            R.string.spt4,
            R.string.spt5,
            R.string.spt6
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
            {"Photosynthesis", "Respiration", "Fermentation", "Krebs cycle"},
            {"Meiosis", "Mitosis", "Respiration", "Fermentation"},
            {"Cheetah", "Peregrine Falcon", "Sailfish", "Black Marlin"},
            {"208", "212", "206", "210"},
            {"3", "4", "5", "6"},
            {"Atlantic", "Pacific", "Indian", "Southern"},
            // Art
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
            {"Copper", "Silver", "Gold", "Aluminum"},
            {"Electricity", "Diesel", "Gasoline", "Natural gas"},
            // Astrology
            {"Mars", "Saturn", "Jupiter", "Uranus"},
            {"Neptune", "Mercury", "Venus", "Mars"},
            {"A type of planet in the outer solar system", "An explosive event that marks the death of a star", "A type of black hole that emits intense radiation", "A method of propulsion used by spacecraft"},
            {"Nebula", "Comet", "Galaxy", "Asteroid"},
            // History
            {"Augustus", "Julius Caesar", "Nero", "Caligula"},
            {"1786", "1787", "1788", "1789"},
            {"USA", "China", "URSS", "Japan"},
            {"Pearl Harbor attack", "Battle of Stalingrad", "D-Day invasion", "Atomic bombing of Hiroshima"},
            {"Mikhail Gorbachev", "Vladimir Lenin", "Joseph Stalin", "Leon Trotsky"},
            {"Television", "Lightbulb", "Automobile", "Telephone"},
            //Sport
            {"5", "11", "8", "6"},
            {"8 feet (2.44 meters)", "10 feet (3.05 meters)", "9 feet (2.74 meters)", "7 feet (2.13 meters)"},
            {"French Open", "US Open", "Australian Open", "Wimbledon"},
            {"2 points", "4 points", "6 points", "3 points"},
            {"Vuelta a España", "Tour de Suisse", "Giro d'Italia", "Tour de France"},
            {"4 outs", "3 outs", "5 outs", "2 outs"}
    };

    public static int correctAnswer[] = {
            // Geography
            1, 2, 2, 3, 0, 3, 3, 1, 1,
            // Science
            3, 2, 0, 1, 0, 1, 0, 2, 0, 1,
            // Art and Culture
            1, 2, 1, 0, 2,
            // Technology
            2, 2, 3, 3, 1, 0,
            // Astrology
            2, 1, 1, 0,
            //History
            0, 3, 2, 0, 1, 3,
            //Sport
            1, 1, 2, 2, 3, 0
    };

    public static int category[] = {
            R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography,
            R.string.science, R.string.science, R.string.science, R.string.science, R.string.science, R.string.science, R.string.science,R.string.science,R.string.science,R.string.science,
            R.string.art, R.string.art, R.string.art, R.string.art, R.string.art,
            R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno,
            R.string.astro, R.string.astro,R.string.astro, R.string.astro,
            R.string.histo, R.string.histo,R.string.histo,R.string.histo,R.string.histo,R.string.histo,
            R.string.sport, R.string.sport,R.string.sport,R.string.sport,R.string.sport,R.string.sport,
    };

}



