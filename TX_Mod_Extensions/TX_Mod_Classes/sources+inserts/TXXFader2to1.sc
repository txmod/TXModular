// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXXFader2to1 : TXModuleBase {

	classvar <>classData;

	var displayOption;
	var arrCurve1Values;
	var arrCurve2Values;
	var arrSlotData, curve1DataEvent, curve2DataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "X-Fader 2-1";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["X-Fade", 1, "modXFade", 0],
		];
		classData.arrAudSCInBusSpecs = [
			["Input 1", 1, "input1"],
			["Input 2", 1, "input2"]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumFade1", 128, 1], ["bufnumFade2", 128, 1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		curve1DataEvent = ();
		curve2DataEvent = ();
		extraLatency = 0.05;	// allow extra time when recreating
		displayOption = "showFade1";
		arrSynthArgSpecs = [
			["input1", 0, 0],
			["input2", 0, 0],
			["out", 0, 0],
			["bufnumFade1", 0, \ir],
			["bufnumFade2", 0, \ir],
			["level1", 1, defLagTime],
			["level2", 1, defLagTime],
			["xFade", 0.5, defLagTime],
			["modXFade", 0, defLagTime],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [1];
		arrOptionData = [
			// use fade 1 for fade 2
			[
				["Off", {arg bufnumFade1, bufnumFade2; bufnumFade2;}],
				["On", {arg bufnumFade1, bufnumFade2; bufnumFade1;}]
			],
		];
		synthDefFunc = { arg input1, input2, out, bufnumFade1, bufnumFade2, level1, level2, xFade, modXFade=0;
			var holdInput1, holdInput2, holdXFade;
			var startEnv = TXEnvPresets.startEnvFunc.value;
			var curveFade1, curveFade2, buf2;

			holdInput1 = InFeedback.ar(input1, 1) * level1;
			holdInput2 = InFeedback.ar(input2, 1) * level2;
			holdXFade = (xFade + modXFade).max(0).min(1);
			curveFade1 =  BufRd.kr(1, bufnumFade1, (1-holdXFade) * 127, 0, 0);
			buf2 = this.getSynthOption(0).value(bufnumFade1, bufnumFade2);
			curveFade2 =  BufRd.kr(1, buf2, holdXFade * 127, 0, 0);

			Out.ar(out, startEnv * Mix.new([(holdInput1 * curveFade1), (holdInput2 * curveFade2)]));
		};
		arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
		arrGridPresetActions = [
			{this.setSynthArgSpec("gridRows", 1); this.setSynthArgSpec("gridCols", 1); },
			{this.setSynthArgSpec("gridRows", 2); this.setSynthArgSpec("gridCols", 2); },
			{this.setSynthArgSpec("gridRows", 3); this.setSynthArgSpec("gridCols", 3); },
			{this.setSynthArgSpec("gridRows", 4); this.setSynthArgSpec("gridCols", 4); },
			{this.setSynthArgSpec("gridRows", 5); this.setSynthArgSpec("gridCols", 5); },
			{this.setSynthArgSpec("gridRows", 6); this.setSynthArgSpec("gridCols", 6); },
			{this.setSynthArgSpec("gridRows", 8); this.setSynthArgSpec("gridCols", 8); },
			{this.setSynthArgSpec("gridRows", 9); this.setSynthArgSpec("gridCols", 9); },
			{this.setSynthArgSpec("gridRows", 10); this.setSynthArgSpec("gridCols", 10); },
			{this.setSynthArgSpec("gridRows", 12); this.setSynthArgSpec("gridCols", 12); },
			{this.setSynthArgSpec("gridRows", 16); this.setSynthArgSpec("gridCols", 16); },
			{this.setSynthArgSpec("gridRows", 24); this.setSynthArgSpec("gridCols", 24); },
			{this.setSynthArgSpec("gridRows", 32); this.setSynthArgSpec("gridCols", 32); },
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["EZslider", "Level 1", ControlSpec(0, 1), "level1"],
			["EZslider", "Level 2", ControlSpec(0, 1), "level2"],
			["EZslider", "X-Fade", ControlSpec(0, 1), "xFade"],
			["SynthOptionCheckBox", "Use Fade 1 for Fade 2", arrOptionData, 0, 200],
			["TXCurveDraw", "Fade curve 1", {arrCurve1Values},
				{arg view; arrCurve1Values = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(0, view.value);},
				{arrSlotData}, "XFade", nil, nil, nil, "gridRows", "gridCols", nil, nil, curve1DataEvent],
			["TXCurveDraw", "Fade curve 2", {arrCurve2Values},
				{arg view; arrCurve2Values = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(1, view.value);},
				{arrSlotData}, "XFade", nil, nil, nil, "gridRows", "gridCols", nil, nil, curve2DataEvent],
		]);
		//	initialise buffer to linear curve
		arrCurve1Values = Array.newClear(128).seriesFill(0, 1/127);
		arrCurve2Values = Array.newClear(128).seriesFill(0, 1/127);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make the buffer, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurve1Values);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurve1Values.dup(5);
	}


	buildGuiSpecArray {
		guiSpecArray = [
			["EZslider", "Level 1", ControlSpec(0, 1), "level1"],
			["SpacerLine", 4],
			["EZslider", "Level 2", ControlSpec(0, 1), "level2"],
			["SpacerLine", 4],
			["EZslider", "X-Fade", ControlSpec(0, 1), "xFade"],
			["SpacerLine", 8],
			["ActionButton", "Fade 1 Curve", {displayOption = "showFade1";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showFade1")],
			["Spacer", 3],
			["ActionButton", "Fade 2 Curve", {displayOption = "showFade2";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showFade2")],
			["Spacer", 30],
			["SynthOptionCheckBox", "Use Fade 1 for Fade 2", arrOptionData, 0, 200],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showFade1", {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Fade curve 1", {arrCurve1Values},
					{arg view; arrCurve1Values = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(0, view.value);},
					{arrSlotData}, "XFade", nil, nil, nil, "gridRows", "gridCols", nil, nil, curve1DataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
			];
		});
		if (displayOption == "showFade2", {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Fade curve 2", {arrCurve2Values},
					{arg view; arrCurve2Values = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(1, view.value);},
					{arrSlotData}, "XFade", nil, nil, nil, "gridRows", "gridCols", nil, nil, curve2DataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
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

	bufferStore { arg argIndex, argArrayVals;
		buffers.at(argIndex).sendCollection(argArrayVals);
	}

	extraSaveData { // override default method
		^[arrCurve1Values, arrCurve2Values, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		arrCurve1Values = argData.at(0);
		arrCurve2Values = argData.at(1);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurve1Values);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(2);
	}
}

