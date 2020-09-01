// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLogicNonBin : TXModuleBase {

	// NOTE Inputs are clipped to range -1 to +1

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Logic NonBin";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Input 1", 1, "input1", 0],
			["Input 2", 1, "input2", 0],
			["Input 3", 1, "input3", 0],
			["Input 4", 1, "input4", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
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
			["out", 0, 0],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Non-binary AND using inputs 1 & 2 ",
					{arg mod1, mod2;
						// Non-binary AND - output is lowest active input
						min(mod1, mod2);
					}
				],
				["Non-binary NAND using inputs 1 & 2",
					{arg mod1, mod2;
						// Non-binary NAND - output is 1 - lowest active input
						1 - min(mod1, mod2);
					}
				],
				["Non-binary OR using inputs 1 & 2",
					{arg mod1, mod2;
						// Non-binary OR - output is highest active input
						max(mod1, mod2);
					}
				],
				["Non-binary NOR using inputs 1 & 2",
					{arg mod1, mod2;
						// Non-binary NOR - output is 1 - highest active input
						1 - max(mod1, mod2);
					}
				],
				["Non-binary AND using inputs 1 - 3",
					{arg mod1, mod2, mod3;
						// Non-binary AND - output is lowest active input
						min(min(mod1, mod2), mod3);
					}
				],
				["Non-binary NAND using inputs 1 - 3",
					{arg mod1, mod2, mod3;
						// Non-binary NAND - output is 1 - lowest active input
						1 - min(min(mod1, mod2), mod3);
					}
				],
				["Non-binary OR using inputs 1 - 3",
					{arg mod1, mod2, mod3;
						// Non-binary OR - output is highest active input
						max(max(mod1, mod2), mod3);
					}
				],
				["Non-binary NOR using inputs 1 - 3",
					{arg mod1, mod2, mod3;
						// Non-binary NOR - 1 - highest active input
						1 - max(max(mod1, mod2), mod3);
					}
				],
				["Non-binary AND using inputs 1 - 4",
					{arg mod1, mod2, mod3, mod4;
						// Non-binary AND - output is lowest active input
						min(min(min(mod1, mod2), mod3), mod4);
					}
				],
				["Non-binary NAND using inputs 1 - 4",
					{arg mod1, mod2, mod3, mod4;
						// Non-binary NAND - output is 1 - lowest active input
						1 - min(min(min(mod1, mod2), mod3), mod4);
					}
				],
				["Non-binary OR using inputs 1 - 4",
					{arg mod1, mod2, mod3, mod4;
						// Non-binary OR - output is highest active input
						max(max(max(mod1, mod2), mod3), mod4);
					}
				],
				["Non-binary NOR using inputs 1 - 4",
					{arg mod1, mod2, mod3, mod4;
						// Non-binary NOR - 1 - highest active input
						1 - max(max(max(mod1, mod2), mod3), mod4);
					}
				],
			]
		];
		synthDefFunc = { arg input1, input2, input3, input4, out;
			var outFunction, outSignal;
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outSignal = outFunction.value(input1.clip(-1, 1), input2.clip(-1, 1), input3.clip(-1, 1), input4.clip(-1, 1));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Logic Mode", arrOptionData, 0],
			["SpacerLine", 4],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

