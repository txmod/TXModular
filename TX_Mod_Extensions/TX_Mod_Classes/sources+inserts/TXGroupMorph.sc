// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGroupMorph : TXModuleBase {

	classvar <>classData;

	var	midiControlRoutine;
	var	displayOption;
	var arrSnapshots, arrSnapshotNames;
	var arrLoadSnapshotItems, arrStoreSnapshotItems;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Group Morph";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["X Value", 1, "modSliderXVal", 0],
			["Y Value", 1, "modSliderYVal", 0],
		];
		classData.noOutChannels = 15;
		classData.arrOutBusSpecs = [
			["Out 1", [0]],
			["Out 2", [1]],
			["Out 3", [2]],
			["Out 4", [3]],
			["Out 5", [4]],
			["Out 6", [5]],
			["Out 7", [6]],
			["Out 8", [7]],
			["Out 9", [8]],
			["Out 10", [9]],
			["Out 11", [10]],
			["Out 12", [11]],
			["Out 13", [12]],
			["Out 14", [13]],
			["Out 15", [14]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGroupMorph";
		arrSnapshots = 0!15!30;
		arrSnapshotNames = "-----"!30;
		arrOptions = [0,0];
		arrOptionData = [
			[
				["Group Morph",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal, morphFunction;
						morphFunction.value(groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal);
					}
				],
				["Solo Group A ",
					{arg groupA, groupB, groupC, groupD, groupE; groupA; }
				],
				["Solo Group B ",
					{arg groupA, groupB, groupC, groupD, groupE; groupB; }
				],
				["Solo Group C ",
					{arg groupA, groupB, groupC, groupD, groupE; groupC; }
				],
				["Solo Group D ",
					{arg groupA, groupB, groupC, groupD, groupE; groupD; }
				],
				["Solo Group E ",
					{arg groupA, groupB, groupC, groupD, groupE; groupE; }
				],
			],
			[
				["2-D Morph - 4 Groups A - D",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						var groupLeft, groupRight, groupOut;
						//		---Groups A-D on X/Y area:---
						//		GroupA = top left / x=0,y=0
						//		GroupB = top right / x=1,y=0
						//		GroupC = bottom left / x=0,y=1
						//		GroupD = bottom right / x=1,y=1
						groupLeft = (groupA * (1-sliderYTotal)) + (groupC * sliderYTotal);
						groupRight = (groupB * (1-sliderYTotal)) + (groupD * sliderYTotal);
						groupOut = (groupLeft * (1-sliderXTotal)) + (groupRight * sliderXTotal);
					}
				],
				["2-D Morph - 5 Groups A - E",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						//		---Groups A-E on X/Y area:---
						//		GroupA = top left / x=0,y=0
						//		GroupB = top right / x=1,y=0
						//		GroupC = bottom left / x=0,y=1
						//		GroupD = bottom right / x=1,y=1
						//		GroupE = centre / x=0.5,y=0.5
						var groupLeft, groupRight, groupsAtoD, middleBias;
						groupLeft = (groupA * (1-sliderYTotal)) + (groupC * sliderYTotal);
						groupRight = (groupB * (1-sliderYTotal)) + (groupD * sliderYTotal);
						groupsAtoD = (groupLeft * (1-sliderXTotal)) + (groupRight * sliderXTotal);
						middleBias =  ((1-((sliderYTotal-0.5).abs*2)) * (1-((sliderXTotal-0.5).abs*2))).sqrt;
						(groupsAtoD * (1 - middleBias) ) + (groupE * middleBias );
					}
				],
				["Linear Morph - 2 Groups A - B",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 2;
						holdScalar = (total-1).reciprocal;
						[groupA, groupB].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 3 Groups A - C",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 3;
						holdScalar = (total-1).reciprocal;
						[groupA, groupB, groupC].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 4 Groups A - D",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 4;
						holdScalar = (total-1).reciprocal;
						[groupA, groupB, groupC, groupD].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 5 Groups A - E",
					{arg groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 5;
						holdScalar = (total-1).reciprocal;
						[groupA, groupB, groupC, groupD, groupE].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
			],
		];
		arrSynthArgSpecs = [
			["out", 0, 0],
			["valA1", 0, 0],
			["valA2", 0, 0],
			["valA3", 0, 0],
			["valA4", 0, 0],
			["valA5", 0, 0],
			["valA6", 0, 0],
			["valA7", 0, 0],
			["valA8", 0, 0],
			["valA9", 0, 0],
			["valA10", 0, 0],
			["valA11", 0, 0],
			["valA12", 0, 0],
			["valA13", 0, 0],
			["valA14", 0, 0],
			["valA15", 0, 0],
			["valB1", 0, 0],
			["valB2", 0, 0],
			["valB3", 0, 0],
			["valB4", 0, 0],
			["valB5", 0, 0],
			["valB6", 0, 0],
			["valB7", 0, 0],
			["valB8", 0, 0],
			["valB9", 0, 0],
			["valB10", 0, 0],
			["valB11", 0, 0],
			["valB12", 0, 0],
			["valB13", 0, 0],
			["valB14", 0, 0],
			["valB15", 0, 0],
			["valC1", 0, 0],
			["valC2", 0, 0],
			["valC3", 0, 0],
			["valC4", 0, 0],
			["valC5", 0, 0],
			["valC6", 0, 0],
			["valC7", 0, 0],
			["valC8", 0, 0],
			["valC9", 0, 0],
			["valC10", 0, 0],
			["valC11", 0, 0],
			["valC12", 0, 0],
			["valC13", 0, 0],
			["valC14", 0, 0],
			["valC15", 0, 0],
			["valD1", 0, 0],
			["valD2", 0, 0],
			["valD3", 0, 0],
			["valD4", 0, 0],
			["valD5", 0, 0],
			["valD6", 0, 0],
			["valD7", 0, 0],
			["valD8", 0, 0],
			["valD9", 0, 0],
			["valD10", 0, 0],
			["valD11", 0, 0],
			["valD12", 0, 0],
			["valD13", 0, 0],
			["valD14", 0, 0],
			["valD15", 0, 0],
			["valE1", 0, 0],
			["valE2", 0, 0],
			["valE3", 0, 0],
			["valE4", 0, 0],
			["valE5", 0, 0],
			["valE6", 0, 0],
			["valE7", 0, 0],
			["valE8", 0, 0],
			["valE9", 0, 0],
			["valE10", 0, 0],
			["valE11", 0, 0],
			["valE12", 0, 0],
			["valE13", 0, 0],
			["valE14", 0, 0],
			["valE15", 0, 0],
			["sliderXVal", 0, 0],
			["sliderYVal", 0, 0],
			["modSliderXVal", 0, 0],
			["modSliderYVal", 0, 0],
			// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience
			["holdString", " "],
			["name1", ""],
			["name2", ""],
			["name3", ""],
			["name4", ""],
			["name5", ""],
			["name6", ""],
			["name7", ""],
			["name8", ""],
			["name9", ""],
			["name10", ""],
			["name11", ""],
			["name12", ""],
			["name13", ""],
			["name14", ""],
			["name15", ""],
		];
		synthDefFunc = {
			arg out, valA1, valA2, valA3, valA4, valA5, valA6, valA7, valA8, valA9, valA10, valA11, valA12, valA13, valA14, valA15,
			valB1, valB2, valB3, valB4, valB5, valB6, valB7, valB8, valB9, valB10, valB11, valB12, valB13, valB14, valB15,
			valC1, valC2, valC3, valC4, valC5, valC6, valC7, valC8, valC9, valC10, valC11, valC12, valC13, valC14, valC15,
			valD1, valD2, valD3, valD4, valD5, valD6, valD7, valD8, valD9, valD10, valD11, valD12, valD13, valD14, valD15,
			valE1, valE2, valE3, valE4, valE5, valE6, valE7, valE8, valE9, valE10, valE11, valE12, valE13, valE14, valE15,
			sliderXVal, sliderYVal, modSliderXVal=0, modSliderYVal=0;
			var groupA, groupB, groupC, groupD, groupE, sliderXTotal, sliderYTotal, outFunction, morphFunction;

			groupA = [valA1, valA2, valA3, valA4, valA5, valA6, valA7, valA8, valA9, valA10, valA11, valA12, valA13, valA14, valA15];
			groupB = [valB1, valB2, valB3, valB4, valB5, valB6, valB7, valB8, valB9, valB10, valB11, valB12, valB13, valB14, valB15];
			groupC = [valC1, valC2, valC3, valC4, valC5, valC6, valC7, valC8, valC9, valC10, valC11, valC12, valC13, valC14, valC15];
			groupD = [valD1, valD2, valD3, valD4, valD5, valD6, valD7, valD8, valD9, valD10, valD11, valD12, valD13, valD14, valD15];
			groupE = [valE1, valE2, valE3, valE4, valE5, valE6, valE7, valE8, valE9, valE10, valE11, valE12, valE13, valE14, valE15];

			sliderXTotal = (sliderXVal + modSliderXVal).max(0).min(1);
			sliderYTotal = (sliderYVal + modSliderYVal).max(0).min(1);

			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			morphFunction = arrOptionData.at(1).at(arrOptions.at(1)).at(1);

			Out.kr(out,
				outFunction.value(groupA, groupB, groupC, groupD, groupE,
					sliderXTotal, sliderYTotal, morphFunction));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0],
			["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1],
			//		["commandAction", "Load Group A Snapshot : value",
			//			{arg argVal; this.loadSnapshot("valA", argVal-1); },
			//			[ControlSpec(1, 30, step: 1)],],
			//		["commandAction", "Load Group B Snapshot : value",
			//			{arg argVal; this.loadSnapshot("valB", argVal-1); },
			//			[ControlSpec(1, 30, step: 1)],],
			//		["commandAction", "Load Group C Snapshot : value",
			//			{arg argVal; this.loadSnapshot("valC", argVal-1); },
			//			[ControlSpec(1, 30, step: 1)],],
			//		["commandAction", "Load Group D Snapshot : value",
			//			{arg argVal; this.loadSnapshot("valD", argVal-1); },
			//			[ControlSpec(1, 30, step: 1)],],

			["TXPopupAction", "Load Group A Snapshot :",
				{["Load Snapshot..."]++ 30.collect ({ arg item, i; (item+1).asString +"-"+ arrSnapshotNames[item]; })},
				"dummyName",
				{ arg view; if (view.value > 0, {this.loadSnapshot("valA", view.value-1); }); }],
			["TXPopupAction", "Load Group B Snapshot :",
				{["Load Snapshot..."]++ 30.collect ({ arg item, i; (item+1).asString +"-"+ arrSnapshotNames[item]; })},
				"dummyName",
				{ arg view; if (view.value > 0, {this.loadSnapshot("valB", view.value-1); }); }],
			["TXPopupAction", "Load Group C Snapshot :",
				{["Load Snapshot..."]++ 30.collect ({ arg item, i; (item+1).asString +"-"+ arrSnapshotNames[item]; })},
				"dummyName",
				{ arg view; if (view.value > 0, {this.loadSnapshot("valC", view.value-1); }); }],
			["TXPopupAction", "Load Group D Snapshot :",
				{["Load Snapshot..."]++ 30.collect ({ arg item, i; (item+1).asString +"-"+ arrSnapshotNames[item]; })},
				"dummyName",
				{ arg view; if (view.value > 0, {this.loadSnapshot("valD", view.value-1); }); }],
			["TXPopupAction", "Load Group E Snapshot :",
				{["Load Snapshot..."]++ 30.collect ({ arg item, i; (item+1).asString +"-"+ arrSnapshotNames[item]; })},
				"dummyName",
				{ arg view; if (view.value > 0, {this.loadSnapshot("valE", view.value-1); }); }],

			["EZslider", "Value X", ControlSpec(0, 1), "sliderXVal"],
			["EZslider", "Value Y", ControlSpec(0, 1), "sliderYVal"],
			["EZslider", "A 1", ControlSpec(0, 1), "valA1"],
			["EZslider", "A 2", ControlSpec(0, 1), "valA2"],
			["EZslider", "A 3", ControlSpec(0, 1), "valA3"],
			["EZslider", "A 4", ControlSpec(0, 1), "valA4"],
			["EZslider", "A 5", ControlSpec(0, 1), "valA5"],
			["EZslider", "A 6", ControlSpec(0, 1), "valA6"],
			["EZslider", "A 7", ControlSpec(0, 1), "valA7"],
			["EZslider", "A 8", ControlSpec(0, 1), "valA8"],
			["EZslider", "A 9", ControlSpec(0, 1), "valA9"],
			["EZslider", "A 10", ControlSpec(0, 1), "valA10"],
			["EZslider", "A 11", ControlSpec(0, 1), "valA11"],
			["EZslider", "A 12", ControlSpec(0, 1), "valA12"],
			["EZslider", "A 13", ControlSpec(0, 1), "valA13"],
			["EZslider", "A 14", ControlSpec(0, 1), "valA14"],
			["EZslider", "A 15", ControlSpec(0, 1), "valA15"],
			["EZslider", "B 1", ControlSpec(0, 1), "valB1"],
			["EZslider", "B 2", ControlSpec(0, 1), "valB2"],
			["EZslider", "B 3", ControlSpec(0, 1), "valB3"],
			["EZslider", "B 4", ControlSpec(0, 1), "valB4"],
			["EZslider", "B 5", ControlSpec(0, 1), "valB5"],
			["EZslider", "B 6", ControlSpec(0, 1), "valB6"],
			["EZslider", "B 7", ControlSpec(0, 1), "valB7"],
			["EZslider", "B 8", ControlSpec(0, 1), "valB8"],
			["EZslider", "B 9", ControlSpec(0, 1), "valB9"],
			["EZslider", "B 10", ControlSpec(0, 1), "valB10"],
			["EZslider", "B 11", ControlSpec(0, 1), "valB11"],
			["EZslider", "B 12", ControlSpec(0, 1), "valB12"],
			["EZslider", "B 13", ControlSpec(0, 1), "valB13"],
			["EZslider", "B 14", ControlSpec(0, 1), "valB14"],
			["EZslider", "B 15", ControlSpec(0, 1), "valB15"],
			["EZslider", "C 1", ControlSpec(0, 1), "valC1"],
			["EZslider", "C 2", ControlSpec(0, 1), "valC2"],
			["EZslider", "C 3", ControlSpec(0, 1), "valC3"],
			["EZslider", "C 4", ControlSpec(0, 1), "valC4"],
			["EZslider", "C 5", ControlSpec(0, 1), "valC5"],
			["EZslider", "C 6", ControlSpec(0, 1), "valC6"],
			["EZslider", "C 7", ControlSpec(0, 1), "valC7"],
			["EZslider", "C 8", ControlSpec(0, 1), "valC8"],
			["EZslider", "C 9", ControlSpec(0, 1), "valC9"],
			["EZslider", "C 10", ControlSpec(0, 1), "valC10"],
			["EZslider", "C 11", ControlSpec(0, 1), "valC11"],
			["EZslider", "C 12", ControlSpec(0, 1), "valC12"],
			["EZslider", "C 13", ControlSpec(0, 1), "valC13"],
			["EZslider", "C 14", ControlSpec(0, 1), "valC14"],
			["EZslider", "C 15", ControlSpec(0, 1), "valC15"],
			["EZslider", "D 1", ControlSpec(0, 1), "valD1"],
			["EZslider", "D 2", ControlSpec(0, 1), "valD2"],
			["EZslider", "D 3", ControlSpec(0, 1), "valD3"],
			["EZslider", "D 4", ControlSpec(0, 1), "valD4"],
			["EZslider", "D 5", ControlSpec(0, 1), "valD5"],
			["EZslider", "D 6", ControlSpec(0, 1), "valD6"],
			["EZslider", "D 7", ControlSpec(0, 1), "valD7"],
			["EZslider", "D 8", ControlSpec(0, 1), "valD8"],
			["EZslider", "D 9", ControlSpec(0, 1), "valD9"],
			["EZslider", "D 10", ControlSpec(0, 1), "valD10"],
			["EZslider", "D 11", ControlSpec(0, 1), "valD11"],
			["EZslider", "D 12", ControlSpec(0, 1), "valD12"],
			["EZslider", "D 13", ControlSpec(0, 1), "valD13"],
			["EZslider", "D 14", ControlSpec(0, 1), "valD14"],
			["EZslider", "D 15", ControlSpec(0, 1), "valD15"],
			["EZslider", "E 1", ControlSpec(0, 1), "valE1"],
			["EZslider", "E 2", ControlSpec(0, 1), "valE2"],
			["EZslider", "E 3", ControlSpec(0, 1), "valE3"],
			["EZslider", "E 4", ControlSpec(0, 1), "valE4"],
			["EZslider", "E 5", ControlSpec(0, 1), "valE5"],
			["EZslider", "E 6", ControlSpec(0, 1), "valE6"],
			["EZslider", "E 7", ControlSpec(0, 1), "valE7"],
			["EZslider", "E 8", ControlSpec(0, 1), "valE8"],
			["EZslider", "E 9", ControlSpec(0, 1), "valE9"],
			["EZslider", "E 10", ControlSpec(0, 1), "valE10"],
			["EZslider", "E 11", ControlSpec(0, 1), "valE11"],
			["EZslider", "E 12", ControlSpec(0, 1), "valE12"],
			["EZslider", "E 13", ControlSpec(0, 1), "valE13"],
			["EZslider", "E 14", ControlSpec(0, 1), "valE14"],
			["EZslider", "E 15", ControlSpec(0, 1), "valE15"],


		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Group Morph", {displayOption = "showGroupMorph";
				arrOptions[0] = 0; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showGroupMorph")],
			["ActionButton", "Group A", {displayOption = "showGroupA";
				arrOptions[0] = 1; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroupA")],
			["ActionButton", "Group B", {displayOption = "showGroupB";
				arrOptions[0] = 2; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroupB")],
			["ActionButton", "Group C", {displayOption = "showGroupC";
				arrOptions[0] = 3; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroupC")],
			["ActionButton", "Group D", {displayOption = "showGroupD";
				arrOptions[0] = 4; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroupD")],
			["ActionButton", "Group E", {displayOption = "showGroupE";
				arrOptions[0] = 5; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroupE")],
			["DividingLine"],
			["SpacerLine", 6],
		];

		arrLoadSnapshotItems = ["Load Snapshot..."]
		++ 30.collect ({ arg item, i; "Load Snapshot " ++ (item+1).asString +"-"+ arrSnapshotNames[item]; });
		arrStoreSnapshotItems = ["Store Snapshot..."] ++ 30.collect ({ arg item, i; "Store Snapshot " ++ (item+1).asString; });

		if (displayOption == "showGroupMorph", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1, nil,
					{this.buildGuiSpecArray; system.showView;}],
				["SpacerLine", 10],
			];
			if (arrOptions[1] < 2, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Group C", 80, 20, TXColor.paleYellow],
					["Spacer", 250],
					["TextBarLeft", "Group D", 80, 20, TXColor.paleYellow],
					["SpacerLine", 2],
					["TX2DSlider", "Morph X-Y", ControlSpec(0, 1),
						"sliderXVal", "sliderYVal", nil, 250, 334, true],
					["SpacerLine", 2],
					["TextBarLeft", "Group A", 80, 20, TXColor.paleYellow],
					["Spacer", 250],
					["TextBarLeft", "Group B", 80, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 1, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 105],
					["TextBarLeft", "Group E is in the centre of the box", 200,
						20, TXColor.paleYellow]
				];
			});
			if (arrOptions[1] > 1, {
				guiSpecArray = guiSpecArray ++[
					["SpacerLine", 6],
					["Spacer", 60],
					["TextBarLeft", "Group A", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 2, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 212],
					["TextBarLeft", "Group B", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 3, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 69],
					["TextBarLeft", "Group B", 69, 20, TXColor.paleYellow],
					["Spacer", 69],
					["TextBarLeft", "Group C", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 4, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 21],
					["TextBarLeft", "Group B", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "Group C", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "Group D", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 5, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Group B", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Group C", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Group D", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Group E", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] > 1, {
				guiSpecArray = guiSpecArray ++[
					["NextLine"],
					["EZslider", "Morph X", ControlSpec(0, 1), "sliderXVal", nil, 450],
				];
			});
		});
		if (displayOption == "showGroupA", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 260],
				["Spacer", 20],
				["ActionButton", "Clone 1 -> All", {this.cloneVal1GroupA;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["ActionButton", "Randomise", {this.randomiseGroupA;}, 70,
					TXColor.white, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXTextBox", "Name", "name1",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A1", ControlSpec(0, 1), "valA1", nil, 300, 30],
				["TXTextBox", "Name", "name2",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A2", ControlSpec(0, 1), "valA2", nil, 300, 30],
				["TXTextBox", "Name", "name3",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A3", ControlSpec(0, 1), "valA3", nil, 300, 30],
				["TXTextBox", "Name", "name4",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A4", ControlSpec(0, 1), "valA4", nil, 300, 30],
				["TXTextBox", "Name", "name5",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A5", ControlSpec(0, 1), "valA5", nil, 300, 30],
				["TXTextBox", "Name", "name6",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A6", ControlSpec(0, 1), "valA6", nil, 300, 30],
				["TXTextBox", "Name", "name7",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A7", ControlSpec(0, 1), "valA7", nil, 300, 30],
				["TXTextBox", "Name", "name8",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A8", ControlSpec(0, 1), "valA8", nil, 300, 30],
				["TXTextBox", "Name", "name9",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A9", ControlSpec(0, 1), "valA9", nil, 300, 30],
				["TXTextBox", "Name", "name10",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A10", ControlSpec(0, 1), "valA10", nil, 300, 30],
				["TXTextBox", "Name", "name11",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A11", ControlSpec(0, 1), "valA11", nil, 300, 30],
				["TXTextBox", "Name", "name12",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A12", ControlSpec(0, 1), "valA12", nil, 300, 30],
				["TXTextBox", "Name", "name13",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A13", ControlSpec(0, 1), "valA13", nil, 300, 30],
				["TXTextBox", "Name", "name14",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A14", ControlSpec(0, 1), "valA14", nil, 300, 30],
				["TXTextBox", "Name", "name15",nil, 100, 40], ["Spacer", 10],
				["EZslider", "A15", ControlSpec(0, 1), "valA15", nil, 300, 30],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot("valA", holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot("valA", holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
			];
		});
		if (displayOption == "showGroupB", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 260],
				["Spacer", 20],
				["ActionButton", "Clone 1 -> All", {this.cloneVal1GroupB;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["ActionButton", "Randomise", {this.randomiseGroupB;}, 80,
					TXColor.white, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXTextBox", "Name", "name1",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B1", ControlSpec(0, 1), "valB1", nil, 300, 30],
				["TXTextBox", "Name", "name2",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B2", ControlSpec(0, 1), "valB2", nil, 300, 30],
				["TXTextBox", "Name", "name3",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B3", ControlSpec(0, 1), "valB3", nil, 300, 30],
				["TXTextBox", "Name", "name4",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B4", ControlSpec(0, 1), "valB4", nil, 300, 30],
				["TXTextBox", "Name", "name5",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B5", ControlSpec(0, 1), "valB5", nil, 300, 30],
				["TXTextBox", "Name", "name6",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B6", ControlSpec(0, 1), "valB6", nil, 300, 30],
				["TXTextBox", "Name", "name7",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B7", ControlSpec(0, 1), "valB7", nil, 300, 30],
				["TXTextBox", "Name", "name8",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B8", ControlSpec(0, 1), "valB8", nil, 300, 30],
				["TXTextBox", "Name", "name9",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B9", ControlSpec(0, 1), "valB9", nil, 300, 30],
				["TXTextBox", "Name", "name10",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B10", ControlSpec(0, 1), "valB10", nil, 300, 30],
				["TXTextBox", "Name", "name11",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B11", ControlSpec(0, 1), "valB11", nil, 300, 30],
				["TXTextBox", "Name", "name12",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B12", ControlSpec(0, 1), "valB12", nil, 300, 30],
				["TXTextBox", "Name", "name13",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B13", ControlSpec(0, 1), "valB13", nil, 300, 30],
				["TXTextBox", "Name", "name14",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B14", ControlSpec(0, 1), "valB14", nil, 300, 30],
				["TXTextBox", "Name", "name15",nil, 100, 40], ["Spacer", 10],
				["EZslider", "B15", ControlSpec(0, 1), "valB15", nil, 300, 30],
				["SpacerLine", 2],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot("valB", holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					120, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot("valB", holdView.value-1); }); },
					400, TXColor.white, TXColor.sysGuiCol1],
			];
		});
		if (displayOption == "showGroupC", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 260],
				["Spacer", 20],
				["ActionButton", "Clone 1 -> All", {this.cloneVal1GroupC;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["ActionButton", "Randomise", {this.randomiseGroupC;}, 80,
					TXColor.white, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXTextBox", "Name", "name1",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C1", ControlSpec(0, 1), "valC1", nil, 300, 30],
				["TXTextBox", "Name", "name2",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C2", ControlSpec(0, 1), "valC2", nil, 300, 30],
				["TXTextBox", "Name", "name3",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C3", ControlSpec(0, 1), "valC3", nil, 300, 30],
				["TXTextBox", "Name", "name4",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C4", ControlSpec(0, 1), "valC4", nil, 300, 30],
				["TXTextBox", "Name", "name5",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C5", ControlSpec(0, 1), "valC5", nil, 300, 30],
				["TXTextBox", "Name", "name6",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C6", ControlSpec(0, 1), "valC6", nil, 300, 30],
				["TXTextBox", "Name", "name7",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C7", ControlSpec(0, 1), "valC7", nil, 300, 30],
				["TXTextBox", "Name", "name8",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C8", ControlSpec(0, 1), "valC8", nil, 300, 30],
				["TXTextBox", "Name", "name9",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C9", ControlSpec(0, 1), "valC9", nil, 300, 30],
				["TXTextBox", "Name", "name10",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C10", ControlSpec(0, 1), "valC10", nil, 300, 30],
				["TXTextBox", "Name", "name11",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C11", ControlSpec(0, 1), "valC11", nil, 300, 30],
				["TXTextBox", "Name", "name12",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C12", ControlSpec(0, 1), "valC12", nil, 300, 30],
				["TXTextBox", "Name", "name13",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C13", ControlSpec(0, 1), "valC13", nil, 300, 30],
				["TXTextBox", "Name", "name14",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C14", ControlSpec(0, 1), "valC14", nil, 300, 30],
				["TXTextBox", "Name", "name15",nil, 100, 40], ["Spacer", 10],
				["EZslider", "C15", ControlSpec(0, 1), "valC15", nil, 300, 30],
				["SpacerLine", 2],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot("valC", holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					120, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot("valC", holdView.value-1); }); },
					400, TXColor.white, TXColor.sysGuiCol1],
			];
		});
		if (displayOption == "showGroupD", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 260],
				["Spacer", 20],
				["ActionButton", "Clone 1 -> All", {this.cloneVal1GroupD;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["ActionButton", "Randomise", {this.randomiseGroupD;}, 80,
					TXColor.white, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXTextBox", "Name", "name1",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D1", ControlSpec(0, 1), "valD1", nil, 300, 30],
				["TXTextBox", "Name", "name2",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D2", ControlSpec(0, 1), "valD2", nil, 300, 30],
				["TXTextBox", "Name", "name3",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D3", ControlSpec(0, 1), "valD3", nil, 300, 30],
				["TXTextBox", "Name", "name4",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D4", ControlSpec(0, 1), "valD4", nil, 300, 30],
				["TXTextBox", "Name", "name5",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D5", ControlSpec(0, 1), "valD5", nil, 300, 30],
				["TXTextBox", "Name", "name6",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D6", ControlSpec(0, 1), "valD6", nil, 300, 30],
				["TXTextBox", "Name", "name7",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D7", ControlSpec(0, 1), "valD7", nil, 300, 30],
				["TXTextBox", "Name", "name8",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D8", ControlSpec(0, 1), "valD8", nil, 300, 30],
				["TXTextBox", "Name", "name9",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D9", ControlSpec(0, 1), "valD9", nil, 300, 30],
				["TXTextBox", "Name", "name10",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D10", ControlSpec(0, 1), "valD10", nil, 300, 30],
				["TXTextBox", "Name", "name11",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D11", ControlSpec(0, 1), "valD11", nil, 300, 30],
				["TXTextBox", "Name", "name12",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D12", ControlSpec(0, 1), "valD12", nil, 300, 30],
				["TXTextBox", "Name", "name13",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D13", ControlSpec(0, 1), "valD13", nil, 300, 30],
				["TXTextBox", "Name", "name14",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D14", ControlSpec(0, 1), "valD14", nil, 300, 30],
				["TXTextBox", "Name", "name15",nil, 100, 40], ["Spacer", 10],
				["EZslider", "D15", ControlSpec(0, 1), "valD15", nil, 300, 30],
				["SpacerLine", 2],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot("valD", holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					120, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot("valD", holdView.value-1); }); },
					400, TXColor.white, TXColor.sysGuiCol1],
			];
		});
		if (displayOption == "showGroupE", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 260],
				["Spacer", 20],
				["ActionButton", "Clone 1 -> All", {this.cloneVal1GroupE;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["ActionButton", "Randomise", {this.randomiseGroupE;}, 80,
					TXColor.white, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXTextBox", "Name", "name1",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E1", ControlSpec(0, 1), "valE1", nil, 300, 30],
				["TXTextBox", "Name", "name2",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E2", ControlSpec(0, 1), "valE2", nil, 300, 30],
				["TXTextBox", "Name", "name3",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E3", ControlSpec(0, 1), "valE3", nil, 300, 30],
				["TXTextBox", "Name", "name4",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E4", ControlSpec(0, 1), "valE4", nil, 300, 30],
				["TXTextBox", "Name", "name5",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E5", ControlSpec(0, 1), "valE5", nil, 300, 30],
				["TXTextBox", "Name", "name6",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E6", ControlSpec(0, 1), "valE6", nil, 300, 30],
				["TXTextBox", "Name", "name7",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E7", ControlSpec(0, 1), "valE7", nil, 300, 30],
				["TXTextBox", "Name", "name8",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E8", ControlSpec(0, 1), "valE8", nil, 300, 30],
				["TXTextBox", "Name", "name9",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E9", ControlSpec(0, 1), "valE9", nil, 300, 30],
				["TXTextBox", "Name", "name10",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E10", ControlSpec(0, 1), "valE10", nil, 300, 30],
				["TXTextBox", "Name", "name11",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E11", ControlSpec(0, 1), "valE11", nil, 300, 30],
				["TXTextBox", "Name", "name12",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E12", ControlSpec(0, 1), "valE12", nil, 300, 30],
				["TXTextBox", "Name", "name13",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E13", ControlSpec(0, 1), "valE13", nil, 300, 30],
				["TXTextBox", "Name", "name14",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E14", ControlSpec(0, 1), "valE14", nil, 300, 30],
				["TXTextBox", "Name", "name15",nil, 100, 40], ["Spacer", 10],
				["EZslider", "E15", ControlSpec(0, 1), "valE15", nil, 300, 30],
				["SpacerLine", 2],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot("valE", holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					120, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot("valE", holdView.value-1); }); },
					400, TXColor.white, TXColor.sysGuiCol1],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	extraSaveData {
		^[arrSnapshots, arrSnapshotNames];
	}
	loadExtraData {arg argData;
		arrSnapshots = argData[0];
		arrSnapshotNames = argData[1] ?? (" "!30);
		this.buildGuiSpecArray;
	}

	loadSnapshot { arg groupString, snapshotNum;
		var arrVals;
		arrVals = arrSnapshots[snapshotNum];
		15.do({ arg item; this.setSynthValue(groupString ++ (item+1).asString, arrVals[item]);});
		// update view
		system.showViewIfModDisplay(this);
	}

	storeSnapshot { arg groupString, snapshotNum, nameString;
		var arrVals;
		arrVals = 15.collect({ arg item; this.getSynthArgSpec(groupString ++ (item+1).asString);
		});
		arrSnapshots[snapshotNum] = arrVals;
		if (nameString.isNil or: (nameString == " "), {nameString = "Stored " ++ (snapshotNum+1).asString});
		arrSnapshotNames[snapshotNum] = nameString;
		this.buildGuiSpecArray;
		// update view
		system.showViewIfModDisplay(this);
	}

	randomiseGroupA {
		this.setSynthValue("valA1", 1.0.rand);
		this.setSynthValue("valA2", 1.0.rand);
		this.setSynthValue("valA3", 1.0.rand);
		this.setSynthValue("valA4", 1.0.rand);
		this.setSynthValue("valA5", 1.0.rand);
		this.setSynthValue("valA6", 1.0.rand);
		this.setSynthValue("valA7", 1.0.rand);
		this.setSynthValue("valA8", 1.0.rand);
		this.setSynthValue("valA9", 1.0.rand);
		this.setSynthValue("valA10", 1.0.rand);
		this.setSynthValue("valA11", 1.0.rand);
		this.setSynthValue("valA12", 1.0.rand);
		this.setSynthValue("valA13", 1.0.rand);
		this.setSynthValue("valA14", 1.0.rand);
		this.setSynthValue("valA15", 1.0.rand);
		system.flagGuiUpd;
	}

	randomiseGroupB {
		this.setSynthValue("valB1", 1.0.rand);
		this.setSynthValue("valB2", 1.0.rand);
		this.setSynthValue("valB3", 1.0.rand);
		this.setSynthValue("valB4", 1.0.rand);
		this.setSynthValue("valB5", 1.0.rand);
		this.setSynthValue("valB6", 1.0.rand);
		this.setSynthValue("valB7", 1.0.rand);
		this.setSynthValue("valB8", 1.0.rand);
		this.setSynthValue("valB9", 1.0.rand);
		this.setSynthValue("valB10", 1.0.rand);
		this.setSynthValue("valB11", 1.0.rand);
		this.setSynthValue("valB12", 1.0.rand);
		this.setSynthValue("valB13", 1.0.rand);
		this.setSynthValue("valB14", 1.0.rand);
		this.setSynthValue("valB15", 1.0.rand);
		system.flagGuiUpd;
	}

	randomiseGroupC {
		this.setSynthValue("valC1", 1.0.rand);
		this.setSynthValue("valC2", 1.0.rand);
		this.setSynthValue("valC3", 1.0.rand);
		this.setSynthValue("valC4", 1.0.rand);
		this.setSynthValue("valC5", 1.0.rand);
		this.setSynthValue("valC6", 1.0.rand);
		this.setSynthValue("valC7", 1.0.rand);
		this.setSynthValue("valC8", 1.0.rand);
		this.setSynthValue("valC9", 1.0.rand);
		this.setSynthValue("valC10", 1.0.rand);
		this.setSynthValue("valC11", 1.0.rand);
		this.setSynthValue("valC12", 1.0.rand);
		this.setSynthValue("valC13", 1.0.rand);
		this.setSynthValue("valC14", 1.0.rand);
		this.setSynthValue("valC15", 1.0.rand);
		system.flagGuiUpd;
	}

	randomiseGroupD {
		this.setSynthValue("valD1", 1.0.rand);
		this.setSynthValue("valD2", 1.0.rand);
		this.setSynthValue("valD3", 1.0.rand);
		this.setSynthValue("valD4", 1.0.rand);
		this.setSynthValue("valD5", 1.0.rand);
		this.setSynthValue("valD6", 1.0.rand);
		this.setSynthValue("valD7", 1.0.rand);
		this.setSynthValue("valD8", 1.0.rand);
		this.setSynthValue("valD9", 1.0.rand);
		this.setSynthValue("valD10", 1.0.rand);
		this.setSynthValue("valD11", 1.0.rand);
		this.setSynthValue("valD12", 1.0.rand);
		this.setSynthValue("valD13", 1.0.rand);
		this.setSynthValue("valD14", 1.0.rand);
		this.setSynthValue("valD15", 1.0.rand);
		system.flagGuiUpd;
	}

	randomiseGroupE {
		this.setSynthValue("valE1", 1.0.rand);
		this.setSynthValue("valE2", 1.0.rand);
		this.setSynthValue("valE3", 1.0.rand);
		this.setSynthValue("valE4", 1.0.rand);
		this.setSynthValue("valE5", 1.0.rand);
		this.setSynthValue("valE6", 1.0.rand);
		this.setSynthValue("valE7", 1.0.rand);
		this.setSynthValue("valE8", 1.0.rand);
		this.setSynthValue("valE9", 1.0.rand);
		this.setSynthValue("valE10", 1.0.rand);
		this.setSynthValue("valE11", 1.0.rand);
		this.setSynthValue("valE12", 1.0.rand);
		this.setSynthValue("valE13", 1.0.rand);
		this.setSynthValue("valE14", 1.0.rand);
		this.setSynthValue("valE15", 1.0.rand);
		system.flagGuiUpd;
	}

	cloneVal1GroupA {
		var holdVal = this.getSynthArgSpec("valA1");
		this.setSynthValue("valA2", holdVal);
		this.setSynthValue("valA3", holdVal);
		this.setSynthValue("valA4", holdVal);
		this.setSynthValue("valA5", holdVal);
		this.setSynthValue("valA6", holdVal);
		this.setSynthValue("valA7", holdVal);
		this.setSynthValue("valA8", holdVal);
		this.setSynthValue("valA9", holdVal);
		this.setSynthValue("valA10", holdVal);
		this.setSynthValue("valA11", holdVal);
		this.setSynthValue("valA12", holdVal);
		this.setSynthValue("valA13", holdVal);
		this.setSynthValue("valA14", holdVal);
		this.setSynthValue("valA15", holdVal);
		system.flagGuiUpd;
	}

	cloneVal1GroupB {
		var holdVal = this.getSynthArgSpec("valB1");
		this.setSynthValue("valB2", holdVal);
		this.setSynthValue("valB3", holdVal);
		this.setSynthValue("valB4", holdVal);
		this.setSynthValue("valB5", holdVal);
		this.setSynthValue("valB6", holdVal);
		this.setSynthValue("valB7", holdVal);
		this.setSynthValue("valB8", holdVal);
		this.setSynthValue("valB9", holdVal);
		this.setSynthValue("valB10", holdVal);
		this.setSynthValue("valB11", holdVal);
		this.setSynthValue("valB12", holdVal);
		this.setSynthValue("valB13", holdVal);
		this.setSynthValue("valB14", holdVal);
		this.setSynthValue("valB15", holdVal);
		system.flagGuiUpd;
	}

	cloneVal1GroupC {
		var holdVal = this.getSynthArgSpec("valC1");
		this.setSynthValue("valC2", holdVal);
		this.setSynthValue("valC3", holdVal);
		this.setSynthValue("valC4", holdVal);
		this.setSynthValue("valC5", holdVal);
		this.setSynthValue("valC6", holdVal);
		this.setSynthValue("valC7", holdVal);
		this.setSynthValue("valC8", holdVal);
		this.setSynthValue("valC9", holdVal);
		this.setSynthValue("valC10", holdVal);
		this.setSynthValue("valC11", holdVal);
		this.setSynthValue("valC12", holdVal);
		this.setSynthValue("valC13", holdVal);
		this.setSynthValue("valC14", holdVal);
		this.setSynthValue("valC15", holdVal);
		system.flagGuiUpd;
	}

	cloneVal1GroupD {
		var holdVal = this.getSynthArgSpec("valD1");
		this.setSynthValue("valD2", holdVal);
		this.setSynthValue("valD3", holdVal);
		this.setSynthValue("valD4", holdVal);
		this.setSynthValue("valD5", holdVal);
		this.setSynthValue("valD6", holdVal);
		this.setSynthValue("valD7", holdVal);
		this.setSynthValue("valD8", holdVal);
		this.setSynthValue("valD9", holdVal);
		this.setSynthValue("valD10", holdVal);
		this.setSynthValue("valD11", holdVal);
		this.setSynthValue("valD12", holdVal);
		this.setSynthValue("valD13", holdVal);
		this.setSynthValue("valD14", holdVal);
		this.setSynthValue("valD15", holdVal);
		system.flagGuiUpd;
	}

	cloneVal1GroupE {
		var holdVal = this.getSynthArgSpec("valE1");
		this.setSynthValue("valE2", holdVal);
		this.setSynthValue("valE3", holdVal);
		this.setSynthValue("valE4", holdVal);
		this.setSynthValue("valE5", holdVal);
		this.setSynthValue("valE6", holdVal);
		this.setSynthValue("valE7", holdVal);
		this.setSynthValue("valE8", holdVal);
		this.setSynthValue("valE9", holdVal);
		this.setSynthValue("valE10", holdVal);
		this.setSynthValue("valE11", holdVal);
		this.setSynthValue("valE12", holdVal);
		this.setSynthValue("valE13", holdVal);
		this.setSynthValue("valE14", holdVal);
		this.setSynthValue("valE15", holdVal);
		system.flagGuiUpd;
	}

}
