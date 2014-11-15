// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMatrixAudio8x8 : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=740;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Matrix A 8x8";
	moduleRate = "audio";
	moduleType = "source";
	arrCtlSCInBusSpecs = [
		["In 1 -> Out 1", 1, "modIn1Out1", 0],
		["In 1 -> Out 2", 1, "modIn1Out2", 0],
		["In 1 -> Out 3", 1, "modIn1Out3", 0],
		["In 1 -> Out 4", 1, "modIn1Out4", 0],
		["In 1 -> Out 5", 1, "modIn1Out5", 0],
		["In 1 -> Out 6", 1, "modIn1Out6", 0],
		["In 1 -> Out 7", 1, "modIn1Out7", 0],
		["In 1 -> Out 8", 1, "modIn1Out8", 0],

		["In 2 -> Out 1", 1, "modIn2Out1", 0],
		["In 2 -> Out 2", 1, "modIn2Out2", 0],
		["In 2 -> Out 3", 1, "modIn2Out3", 0],
		["In 2 -> Out 4", 1, "modIn2Out4", 0],
		["In 2 -> Out 5", 1, "modIn2Out5", 0],
		["In 2 -> Out 6", 1, "modIn2Out6", 0],
		["In 2 -> Out 7", 1, "modIn2Out7", 0],
		["In 2 -> Out 8", 1, "modIn2Out8", 0],

		["In 3 -> Out 1", 1, "modIn3Out1", 0],
		["In 3 -> Out 2", 1, "modIn3Out2", 0],
		["In 3 -> Out 3", 1, "modIn3Out3", 0],
		["In 3 -> Out 4", 1, "modIn3Out4", 0],
		["In 3 -> Out 5", 1, "modIn3Out5", 0],
		["In 3 -> Out 6", 1, "modIn3Out6", 0],
		["In 3 -> Out 7", 1, "modIn3Out7", 0],
		["In 3 -> Out 8", 1, "modIn3Out8", 0],

		["In 4 -> Out 1", 1, "modIn4Out1", 0],
		["In 4 -> Out 2", 1, "modIn4Out2", 0],
		["In 4 -> Out 3", 1, "modIn4Out3", 0],
		["In 4 -> Out 4", 1, "modIn4Out4", 0],
		["In 4 -> Out 5", 1, "modIn4Out5", 0],
		["In 4 -> Out 6", 1, "modIn4Out6", 0],
		["In 4 -> Out 7", 1, "modIn4Out7", 0],
		["In 4 -> Out 8", 1, "modIn4Out8", 0],

		["In 5 -> Out 1", 1, "modIn5Out1", 0],
		["In 5 -> Out 2", 1, "modIn5Out2", 0],
		["In 5 -> Out 3", 1, "modIn5Out3", 0],
		["In 5 -> Out 4", 1, "modIn5Out4", 0],
		["In 5 -> Out 5", 1, "modIn5Out5", 0],
		["In 5 -> Out 6", 1, "modIn5Out6", 0],
		["In 5 -> Out 7", 1, "modIn5Out7", 0],
		["In 5 -> Out 8", 1, "modIn5Out8", 0],

		["In 6 -> Out 1", 1, "modIn6Out1", 0],
		["In 6 -> Out 2", 1, "modIn6Out2", 0],
		["In 6 -> Out 3", 1, "modIn6Out3", 0],
		["In 6 -> Out 4", 1, "modIn6Out4", 0],
		["In 6 -> Out 5", 1, "modIn6Out5", 0],
		["In 6 -> Out 6", 1, "modIn6Out6", 0],
		["In 6 -> Out 7", 1, "modIn6Out7", 0],
		["In 6 -> Out 8", 1, "modIn6Out8", 0],

		["In 7 -> Out 1", 1, "modIn7Out1", 0],
		["In 7 -> Out 2", 1, "modIn7Out2", 0],
		["In 7 -> Out 3", 1, "modIn7Out3", 0],
		["In 7 -> Out 4", 1, "modIn7Out4", 0],
		["In 7 -> Out 5", 1, "modIn7Out5", 0],
		["In 7 -> Out 6", 1, "modIn7Out6", 0],
		["In 7 -> Out 7", 1, "modIn7Out7", 0],
		["In 7 -> Out 8", 1, "modIn7Out8", 0],

		["In 8 -> Out 1", 1, "modIn8Out1", 0],
		["In 8 -> Out 2", 1, "modIn8Out2", 0],
		["In 8 -> Out 3", 1, "modIn8Out3", 0],
		["In 8 -> Out 4", 1, "modIn8Out4", 0],
		["In 8 -> Out 5", 1, "modIn8Out5", 0],
		["In 8 -> Out 6", 1, "modIn8Out6", 0],
		["In 8 -> Out 7", 1, "modIn8Out7", 0],
		["In 8 -> Out 8", 1, "modIn8Out8", 0],

		["Output Level", 1, "modOutLevel", 0],
		// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience
	];
	arrAudSCInBusSpecs = [
		 ["Input 1", 1, "input1"],
		 ["Input 2", 1, "input2"],
		 ["Input 3", 1, "input3"],
		 ["Input 4", 1, "input4"],
		 ["Input 5", 1, "input5"],
		 ["Input 6", 1, "input6"],
		 ["Input 7", 1, "input7"],
		 ["Input 8", 1, "input8"],
	];
	noOutChannels = 8;
	arrOutBusSpecs = [
		["Out 1 + 2", [0,1]],
		["Out 3 + 4", [2,3]],
		["Out 5 + 6", [4,5]],
		["Out 7 + 8", [6,7]],
		["Out 1", [0]],
		["Out 2", [1]],
		["Out 3", [2]],
		["Out 4", [3]],
		["Out 5", [4]],
		["Out 6", [5]],
		["Out 7", [6]],
		["Out 8", [7]],
	];
}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

init {arg argInstName;
	//	set  class specific instance variables
	arrSynthArgSpecs = [
		["input1", 0, 0],
		["input2", 0, 0],
		["input3", 0, 0],
		["input4", 0, 0],
		["input5", 0, 0],
		["input6", 0, 0],
		["input7", 0, 0],
		["input8", 0, 0],
		["out", 0, 0],

		["in1Out1", 0, defLagTime],
		["in1Out2", 0, defLagTime],
		["in1Out3", 0, defLagTime],
		["in1Out4", 0, defLagTime],
		["in1Out5", 0, defLagTime],
		["in1Out6", 0, defLagTime],
		["in1Out7", 0, defLagTime],
		["in1Out8", 0, defLagTime],

		["in2Out1", 0, defLagTime],
		["in2Out2", 0, defLagTime],
		["in2Out3", 0, defLagTime],
		["in2Out4", 0, defLagTime],
		["in2Out5", 0, defLagTime],
		["in2Out6", 0, defLagTime],
		["in2Out7", 0, defLagTime],
		["in2Out8", 0, defLagTime],

		["in3Out1", 0, defLagTime],
		["in3Out2", 0, defLagTime],
		["in3Out3", 0, defLagTime],
		["in3Out4", 0, defLagTime],
		["in3Out5", 0, defLagTime],
		["in3Out6", 0, defLagTime],
		["in3Out7", 0, defLagTime],
		["in3Out8", 0, defLagTime],

		["in4Out1", 0, defLagTime],
		["in4Out2", 0, defLagTime],
		["in4Out3", 0, defLagTime],
		["in4Out4", 0, defLagTime],
		["in4Out5", 0, defLagTime],
		["in4Out6", 0, defLagTime],
		["in4Out7", 0, defLagTime],
		["in4Out8", 0, defLagTime],

		["in5Out1", 0, defLagTime],
		["in5Out2", 0, defLagTime],
		["in5Out3", 0, defLagTime],
		["in5Out4", 0, defLagTime],
		["in5Out5", 0, defLagTime],
		["in5Out6", 0, defLagTime],
		["in5Out7", 0, defLagTime],
		["in5Out8", 0, defLagTime],

		["in6Out1", 0, defLagTime],
		["in6Out2", 0, defLagTime],
		["in6Out3", 0, defLagTime],
		["in6Out4", 0, defLagTime],
		["in6Out5", 0, defLagTime],
		["in6Out6", 0, defLagTime],
		["in6Out7", 0, defLagTime],
		["in6Out8", 0, defLagTime],

		["in7Out1", 0, defLagTime],
		["in7Out2", 0, defLagTime],
		["in7Out3", 0, defLagTime],
		["in7Out4", 0, defLagTime],
		["in7Out5", 0, defLagTime],
		["in7Out6", 0, defLagTime],
		["in7Out7", 0, defLagTime],
		["in7Out8", 0, defLagTime],

		["in8Out1", 0, defLagTime],
		["in8Out2", 0, defLagTime],
		["in8Out3", 0, defLagTime],
		["in8Out4", 0, defLagTime],
		["in8Out5", 0, defLagTime],
		["in8Out6", 0, defLagTime],
		["in8Out7", 0, defLagTime],
		["in8Out8", 0, defLagTime],

		["outLevel", 0.5, defLagTime],

		["modIn1Out1", 0, defLagTime],
		["modIn1Out2", 0, defLagTime],
		["modIn1Out3", 0, defLagTime],
		["modIn1Out4", 0, defLagTime],
		["modIn1Out5", 0, defLagTime],
		["modIn1Out6", 0, defLagTime],
		["modIn1Out7", 0, defLagTime],
		["modIn1Out8", 0, defLagTime],

		["modIn2Out1", 0, defLagTime],
		["modIn2Out2", 0, defLagTime],
		["modIn2Out3", 0, defLagTime],
		["modIn2Out4", 0, defLagTime],
		["modIn2Out5", 0, defLagTime],
		["modIn2Out6", 0, defLagTime],
		["modIn2Out7", 0, defLagTime],
		["modIn2Out8", 0, defLagTime],

		["modIn3Out1", 0, defLagTime],
		["modIn3Out2", 0, defLagTime],
		["modIn3Out3", 0, defLagTime],
		["modIn3Out4", 0, defLagTime],
		["modIn3Out5", 0, defLagTime],
		["modIn3Out6", 0, defLagTime],
		["modIn3Out7", 0, defLagTime],
		["modIn3Out8", 0, defLagTime],

		["modIn4Out1", 0, defLagTime],
		["modIn4Out2", 0, defLagTime],
		["modIn4Out3", 0, defLagTime],
		["modIn4Out4", 0, defLagTime],
		["modIn4Out5", 0, defLagTime],
		["modIn4Out6", 0, defLagTime],
		["modIn4Out7", 0, defLagTime],
		["modIn4Out8", 0, defLagTime],

		["modIn5Out1", 0, defLagTime],
		["modIn5Out2", 0, defLagTime],
		["modIn5Out3", 0, defLagTime],
		["modIn5Out4", 0, defLagTime],
		["modIn5Out5", 0, defLagTime],
		["modIn5Out6", 0, defLagTime],
		["modIn5Out7", 0, defLagTime],
		["modIn5Out8", 0, defLagTime],

		["modIn6Out1", 0, defLagTime],
		["modIn6Out2", 0, defLagTime],
		["modIn6Out3", 0, defLagTime],
		["modIn6Out4", 0, defLagTime],
		["modIn6Out5", 0, defLagTime],
		["modIn6Out6", 0, defLagTime],
		["modIn6Out7", 0, defLagTime],
		["modIn6Out8", 0, defLagTime],

		["modIn7Out1", 0, defLagTime],
		["modIn7Out2", 0, defLagTime],
		["modIn7Out3", 0, defLagTime],
		["modIn7Out4", 0, defLagTime],
		["modIn7Out5", 0, defLagTime],
		["modIn7Out6", 0, defLagTime],
		["modIn7Out7", 0, defLagTime],
		["modIn7Out8", 0, defLagTime],

		["modIn8Out1", 0, defLagTime],
		["modIn8Out2", 0, defLagTime],
		["modIn8Out3", 0, defLagTime],
		["modIn8Out4", 0, defLagTime],
		["modIn8Out5", 0, defLagTime],
		["modIn8Out6", 0, defLagTime],
		["modIn8Out7", 0, defLagTime],
		["modIn8Out8", 0, defLagTime],

		["modOutLevel", 0, defLagTime],
		["nameIn1", ""],
		["nameIn2", ""],
		["nameIn3", ""],
		["nameIn4", ""],
		["nameIn5", ""],
		["nameIn6", ""],
		["nameIn7", ""],
		["nameIn8", ""],
		["nameOut1", ""],
		["nameOut2", ""],
		["nameOut3", ""],
		["nameOut4", ""],
		["nameOut5", ""],
		["nameOut6", ""],
		["nameOut7", ""],
		["nameOut8", ""],
	];
	synthDefFunc = { arg input1, input2, input3, input4, input5, input6, input7, input8,
			out,
			in1Out1, in1Out2, in1Out3, in1Out4, in1Out5, in1Out6, in1Out7, in1Out8, in2Out1, in2Out2, in2Out3, in2Out4, in2Out5, in2Out6, in2Out7, in2Out8, in3Out1, in3Out2, in3Out3, in3Out4, in3Out5, in3Out6, in3Out7, in3Out8, in4Out1, in4Out2, in4Out3, in4Out4, in4Out5, in4Out6, in4Out7, in4Out8, in5Out1, in5Out2, in5Out3, in5Out4, in5Out5, in5Out6, in5Out7, in5Out8, in6Out1, in6Out2, in6Out3, in6Out4, in6Out5, in6Out6, in6Out7, in6Out8, in7Out1, in7Out2, in7Out3, in7Out4, in7Out5, in7Out6, in7Out7, in7Out8, in8Out1, in8Out2, in8Out3, in8Out4, in8Out5, in8Out6, in8Out7, in8Out8,
			outLevel,
			 modIn1Out1, modIn1Out2, modIn1Out3, modIn1Out4, modIn1Out5, modIn1Out6, modIn1Out7, modIn1Out8, modIn2Out1, modIn2Out2, modIn2Out3, modIn2Out4, modIn2Out5, modIn2Out6, modIn2Out7, modIn2Out8, modIn3Out1, modIn3Out2, modIn3Out3, modIn3Out4, modIn3Out5, modIn3Out6, modIn3Out7, modIn3Out8, modIn4Out1, modIn4Out2, modIn4Out3, modIn4Out4, modIn4Out5, modIn4Out6, modIn4Out7, modIn4Out8, modIn5Out1, modIn5Out2, modIn5Out3, modIn5Out4, modIn5Out5, modIn5Out6, modIn5Out7, modIn5Out8, modIn6Out1, modIn6Out2, modIn6Out3, modIn6Out4, modIn6Out5, modIn6Out6, modIn6Out7, modIn6Out8, modIn7Out1, modIn7Out2, modIn7Out3, modIn7Out4, modIn7Out5, modIn7Out6, modIn7Out7, modIn7Out8, modIn8Out1, modIn8Out2, modIn8Out3, modIn8Out4, modIn8Out5, modIn8Out6, modIn8Out7, modIn8Out8,
			modOutLevel;

		var arrInOuts, arrModInOuts, arrSums;
		var holdInput1, holdInput2, holdInput3, holdInput4, holdInput5, holdInput6, holdInput7,
			holdInput8, holdOutLevel;

		arrInOuts = [in1Out1, in1Out2, in1Out3, in1Out4, in1Out5, in1Out6, in1Out7, in1Out8, in2Out1, in2Out2, in2Out3, in2Out4, in2Out5, in2Out6, in2Out7, in2Out8, in3Out1, in3Out2, in3Out3, in3Out4, in3Out5, in3Out6, in3Out7, in3Out8, in4Out1, in4Out2, in4Out3, in4Out4, in4Out5, in4Out6, in4Out7, in4Out8, in5Out1, in5Out2, in5Out3, in5Out4, in5Out5, in5Out6, in5Out7, in5Out8, in6Out1, in6Out2, in6Out3, in6Out4, in6Out5, in6Out6, in6Out7, in6Out8, in7Out1, in7Out2, in7Out3, in7Out4, in7Out5, in7Out6, in7Out7, in7Out8, in8Out1, in8Out2, in8Out3, in8Out4, in8Out5, in8Out6, in8Out7, in8Out8];

		arrModInOuts = [modIn1Out1, modIn1Out2, modIn1Out3, modIn1Out4, modIn1Out5, modIn1Out6, modIn1Out7, modIn1Out8, modIn2Out1, modIn2Out2, modIn2Out3, modIn2Out4, modIn2Out5, modIn2Out6, modIn2Out7, modIn2Out8, modIn3Out1, modIn3Out2, modIn3Out3, modIn3Out4, modIn3Out5, modIn3Out6, modIn3Out7, modIn3Out8, modIn4Out1, modIn4Out2, modIn4Out3, modIn4Out4, modIn4Out5, modIn4Out6, modIn4Out7, modIn4Out8, modIn5Out1, modIn5Out2, modIn5Out3, modIn5Out4, modIn5Out5, modIn5Out6, modIn5Out7, modIn5Out8, modIn6Out1, modIn6Out2, modIn6Out3, modIn6Out4, modIn6Out5, modIn6Out6, modIn6Out7, modIn6Out8, modIn7Out1, modIn7Out2, modIn7Out3, modIn7Out4, modIn7Out5, modIn7Out6, modIn7Out7, modIn7Out8, modIn8Out1, modIn8Out2, modIn8Out3, modIn8Out4, modIn8Out5, modIn8Out6, modIn8Out7, modIn8Out8];

		arrSums = (arrInOuts + arrModInOuts).max(0).min(1);

		holdInput1 = InFeedback.ar(input1, 1);
		holdInput2 = InFeedback.ar(input2, 1);
		holdInput3 = InFeedback.ar(input3, 1);
		holdInput4 = InFeedback.ar(input4, 1);
		holdInput5 = InFeedback.ar(input5, 1);
		holdInput6 = InFeedback.ar(input6, 1);
		holdInput7 = InFeedback.ar(input7, 1);
		holdInput8 = InFeedback.ar(input8, 1);

		holdOutLevel = (outLevel + modOutLevel).max(0).min(1);
		Out.ar(out, holdOutLevel *
			arrSums.clump(8).flop.collect({ arg item, i;
				Mix.new(
					[holdInput1, holdInput2, holdInput3, holdInput4,
						holdInput5, holdInput6, holdInput7, holdInput8]
					* item;
				);
			});
		);
	};
	guiSpecArray = [
		["SpacerLine", 2],
		["TextBar", "Output Names", 158],
		["TXTextBox", "", "nameOut1",nil, 50, 0],
		["TXTextBox", "", "nameOut2",nil, 50, 0],
		["TXTextBox", "", "nameOut3",nil, 50, 0],
		["TXTextBox", "", "nameOut4",nil, 50, 0],
		["TXTextBox", "", "nameOut5",nil, 50, 0],
		["TXTextBox", "", "nameOut6",nil, 50, 0],
		["TXTextBox", "", "nameOut7",nil, 50, 0],
		["TXTextBox", "", "nameOut8",nil, 50, 0],
		["NextLine"],
		["TextBar", "Input Names", 158],
		["TextBar", "Output 1", 50],
		["TextBar", "Output 2", 50],
		["TextBar", "Output 3", 50],
		["TextBar", "Output 4", 50],
		["TextBar", "Output 5", 50],
		["TextBar", "Output 6", 50],
		["TextBar", "Output 7", 50],
		["TextBar", "Output 8", 50],
		["NextLine"],
		["TXTextBox", "Name", "nameIn1",nil, 70, 40],
		["TXMultiKnob", "Input 1", ["in1Out1", "in1Out2", "in1Out3", "in1Out4",
			"in1Out5", "in1Out6", "in1Out7", "in1Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn2",nil, 70, 40],
		["TXMultiKnob", "Input 2", ["in2Out1", "in2Out2", "in2Out3", "in2Out4",
			"in2Out5", "in2Out6", "in2Out7", "in2Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn3",nil, 70, 40],
		["TXMultiKnob", "Input 3", ["in3Out1", "in3Out2", "in3Out3", "in3Out4",
			"in3Out5", "in3Out6", "in3Out7", "in3Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn4",nil, 70, 40],
		["TXMultiKnob", "Input 4", ["in4Out1", "in4Out2", "in4Out3", "in4Out4",
			"in4Out5", "in4Out6", "in4Out7", "in4Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn5",nil, 70, 40],
		["TXMultiKnob", "Input 5", ["in5Out1", "in5Out2", "in5Out3", "in5Out4",
			"in5Out5", "in5Out6", "in5Out7", "in5Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn6",nil, 70, 40],
		["TXMultiKnob", "Input 6", ["in6Out1", "in6Out2", "in6Out3", "in6Out4",
			"in6Out5", "in6Out6", "in6Out7", "in6Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn7",nil, 70, 40],
		["TXMultiKnob", "Input 7", ["in7Out1", "in7Out2", "in7Out3", "in7Out4",
			"in7Out5", "in7Out6", "in7Out7", "in7Out8", ], 8, ControlSpec(0, 1)],
		["NextLine"],
		["TXTextBox", "Name", "nameIn8",nil, 70, 40],
		["TXMultiKnob", "Input 8", ["in8Out1", "in8Out2", "in8Out3", "in8Out4",
			"in8Out5", "in8Out6", "in8Out7", "in8Out8", ], 8, ControlSpec(0, 1)],
		["SpacerLine", 20],
		["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
	];
	arrActionSpecs = this.buildActionSpecs(guiSpecArray);
	//	use base class initialise
	this.baseInit(this, argInstName);
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

}

