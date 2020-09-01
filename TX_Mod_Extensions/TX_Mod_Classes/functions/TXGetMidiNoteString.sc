// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXGetMidiNoteString { // note number to midi string conversion

	classvar <>arrAllNoteNames,	<>arrAllNoteNumberNames;

	*initClass {
		arrAllNoteNames = 128.collect ({arg item, i; this.new(item);});
		arrAllNoteNumberNames = 128.collect ({arg item; item.asString ++ " " ++ arrAllNoteNames[item];});
	}

	*new { arg note;
		var noteText, octText;
		note = note.asInteger.max(0).min(128);
		noteText =
		["C", "C#", "D", "D#", "E", "F", "F#", "G","G#", "A", "A#", "B"]
		.at(note % 12);
		octText = ((note div: 12) - 2).asString;
		^noteText ++ octText;
	}

}
