# Markov-Note
A Simple Melody Generator Using Markov Chain

Inspired by the program WordNGram by Duke University, Markov Note is a simple melody-generator based on the Markov Chain. By reading an input melody and mapping out the sequences (states), the program follow the Markov stochastic model to output a new melody. 

Since the melody input is designed to be a text file which contains notes in the format of “pitch rhythm,” the program can interpret the melody in two ways: MarkovGeneral and MarkovCombine. It can map the sequence of the pitch and the rhythm separately or map the sequence of the notes by treating each of them as a whole. The “degree” textbox in the GUI refers to the length of the sequence to be mapped before a preceding note, pitch, or rhythm.

# Example

## Input melody - (*follows the format of JFugue)
* Eb5 i
* F5 i
* Gb5 i
* Ab5 i
* Bb5 q
* Eb6 i
* Db6 i
* Bb5 q
* Eb5 q
* Bb5 i
* Ab5 i
* Gb5 i
* F5 i

## Interpretation choice - MarkovGeneral
* Pitch Degree - 1
* Rhythm Degree - 2

## Sequence Mapped:
Pitch: 
* [Eb5] [F5, Bb5] // Eb5 is followed by F5 in the beginning and by Bb5 later
* [F5] [Gb5]
* [Gb5] [Ab5, F5]
* …...

## Rhythm:
* [i, i] [i, i, q, q, i, i] // A consecutive set of i, i is followed by another i in the beginning and by q later
* [i, q] [i, q]
* ……

# Getting Started
Feel free to play with the program using the melodies in the data folder.
