// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSpectralFX : TXModuleBase {		// Spectral FX module

	// This module uses spectral processes from the class TXPVProcess

	classvar <>classData;

	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Spectral FX";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Side Chain", 1, "inSideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Proc 1 Mod 1", 1, "modProc1Mod1", 0],
			["Proc 1 Mod 2", 1, "modProc1Mod2", 0],
			["Proc 1 Mod 3", 1, "modProc1Mod3", 0],
			["Proc 2 Mod 1", 1, "modProc2Mod1", 0],
			["Proc 2 Mod 2", 1, "modProc2Mod2", 0],
			["Proc 2 Mod 3", 1, "modProc2Mod3", 0],
			["Proc 3 Mod 1", 1, "modProc3Mod1", 0],
			["Proc 3 Mod 2", 1, "modProc3Mod2", 0],
			["Proc 3 Mod 3", 1, "modProc3Mod3", 0],
			["Proc 4 Mod 1", 1, "modProc4Mod1", 0],
			["Proc 4 Mod 2", 1, "modProc4Mod2", 0],
			["Proc 4 Mod 3", 1, "modProc4Mod3", 0],
			["Proc 5 Mod 1", 1, "modProc5Mod1", 0],
			["Proc 5 Mod 2", 1, "modProc5Mod2", 0],
			["Proc 5 Mod 3", 1, "modProc5Mod3", 0],
			["Proc 6 Mod 1", 1, "modProc6Mod1", 0],
			["Proc 6 Mod 2", 1, "modProc6Mod2", 0],
			["Proc 6 Mod 3", 1, "modProc6Mod3", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
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
		displayOption = "showSettings";
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["inSideChain", 0, 0],
			["out", 0, 0],
			//		["bufnumMain", 0, 0],
			//		["bufnumSideChain", 0, 0],
			["proc1Mod1", 0.5, defLagTime],
			["proc1Mod1Min", 0, defLagTime],
			["proc1Mod1Max", 1, defLagTime],
			["proc1Mod2", 0.5, defLagTime],
			["proc1Mod2Min", 0, defLagTime],
			["proc1Mod2Max", 1, defLagTime],
			["proc1Mod3", 0.5, defLagTime],
			["proc1Mod3Min", 0, defLagTime],
			["proc1Mod3Max", 1, defLagTime],
			["proc2Mod1", 0.5, defLagTime],
			["proc2Mod1Min", 0, defLagTime],
			["proc2Mod1Max", 1, defLagTime],
			["proc2Mod2", 0.5, defLagTime],
			["proc2Mod2Min", 0, defLagTime],
			["proc2Mod2Max", 1, defLagTime],
			["proc2Mod3", 0.5, defLagTime],
			["proc2Mod3Min", 0, defLagTime],
			["proc2Mod3Max", 1, defLagTime],
			["proc3Mod1", 0.5, defLagTime],
			["proc3Mod1Min", 0, defLagTime],
			["proc3Mod1Max", 1, defLagTime],
			["proc3Mod2", 0.5, defLagTime],
			["proc3Mod2Min", 0, defLagTime],
			["proc3Mod2Max", 1, defLagTime],
			["proc3Mod3", 0.5, defLagTime],
			["proc3Mod3Min", 0, defLagTime],
			["proc3Mod3Max", 1, defLagTime],
			["proc4Mod1", 0.5, defLagTime],
			["proc4Mod1Min", 0, defLagTime],
			["proc4Mod1Max", 1, defLagTime],
			["proc4Mod2", 0.5, defLagTime],
			["proc4Mod2Min", 0, defLagTime],
			["proc4Mod2Max", 1, defLagTime],
			["proc4Mod3", 0.5, defLagTime],
			["proc4Mod3Min", 0, defLagTime],
			["proc4Mod3Max", 1, defLagTime],
			["proc5Mod1", 0.5, defLagTime],
			["proc5Mod1Min", 0, defLagTime],
			["proc5Mod1Max", 1, defLagTime],
			["proc5Mod2", 0.5, defLagTime],
			["proc5Mod2Min", 0, defLagTime],
			["proc5Mod2Max", 1, defLagTime],
			["proc5Mod3", 0.5, defLagTime],
			["proc5Mod3Min", 0, defLagTime],
			["proc5Mod3Max", 1, defLagTime],
			["proc6Mod1", 0.5, defLagTime],
			["proc6Mod1Min", 0, defLagTime],
			["proc6Mod1Max", 1, defLagTime],
			["proc6Mod2", 0.5, defLagTime],
			["proc6Mod2Min", 0, defLagTime],
			["proc6Mod2Max", 1, defLagTime],
			["proc6Mod3", 0.5, defLagTime],
			["proc6Mod3Min", 0, defLagTime],
			["proc6Mod3Max", 1, defLagTime],

			["wetDryMix", 1.0, defLagTime],

			["modProc1Mod1", 0, defLagTime],
			["modProc1Mod2", 0, defLagTime],
			["modProc1Mod3", 0, defLagTime],
			["modProc2Mod1", 0, defLagTime],
			["modProc2Mod2", 0, defLagTime],
			["modProc2Mod3", 0, defLagTime],
			["modProc3Mod1", 0, defLagTime],
			["modProc3Mod2", 0, defLagTime],
			["modProc3Mod3", 0, defLagTime],
			["modProc4Mod1", 0, defLagTime],
			["modProc4Mod2", 0, defLagTime],
			["modProc4Mod3", 0, defLagTime],
			["modProc5Mod1", 0, defLagTime],
			["modProc5Mod2", 0, defLagTime],
			["modProc5Mod3", 0, defLagTime],
			["modProc6Mod1", 0, defLagTime],
			["modProc6Mod2", 0, defLagTime],
			["modProc6Mod3", 0, defLagTime],

			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0,0,0,0,0,0,2,0,0];
		arrOptionData = [
			TXPVProcess.arrOptionData,
			TXPVProcess.arrOptionData,
			TXPVProcess.arrOptionData,
			TXPVProcess.arrOptionData,
			TXPVProcess.arrOptionData,
			TXPVProcess.arrOptionData,
			// fft size
			[["128", 128], ["256", 256], ["512 - default", 512], ["1024", 1024], ["2048", 2048] , ["4096", 4096]],
			// fft window
			[["Sine - default 1", 0], ["Hanning - default 2", 1], ["Rectangular", -1]],
			// fft hop
			[["50 percent - default 1", 0.5], ["25 percent - default 2", 0.25]],
		];
		synthDefFunc = { arg in, inSideChain, out,
			//		bufnumMain, bufnumSideChain,
			proc1Mod1, proc1Mod1Min, proc1Mod1Max, proc1Mod2, proc1Mod2Min, proc1Mod2Max, proc1Mod3, proc1Mod3Min, proc1Mod3Max,
			proc2Mod1, proc2Mod1Min, proc2Mod1Max, proc2Mod2, proc2Mod2Min, proc2Mod2Max, proc2Mod3, proc2Mod3Min, proc2Mod3Max,
			proc3Mod1, proc3Mod1Min, proc3Mod1Max, proc3Mod2, proc3Mod2Min, proc3Mod2Max, proc3Mod3, proc3Mod3Min, proc3Mod3Max,
			proc4Mod1, proc4Mod1Min, proc4Mod1Max, proc4Mod2, proc4Mod2Min, proc4Mod2Max, proc4Mod3, proc4Mod3Min, proc4Mod3Max,
			proc5Mod1, proc5Mod1Min, proc5Mod1Max, proc5Mod2, proc5Mod2Min, proc5Mod2Max, proc5Mod3, proc5Mod3Min, proc5Mod3Max,
			proc6Mod1, proc6Mod1Min, proc6Mod1Max, proc6Mod2, proc6Mod2Min, proc6Mod2Max, proc6Mod3, proc6Mod3Min, proc6Mod3Max,
			wetDryMix,
			modProc1Mod1 = 0, modProc1Mod2 = 0, modProc1Mod3 = 0, modProc2Mod1 = 0, modProc2Mod2 = 0, modProc2Mod3 = 0,
			modProc3Mod1 = 0, modProc3Mod2 = 0, modProc3Mod3 = 0, modProc4Mod1 = 0, modProc4Mod2 = 0, modProc4Mod3 = 0,
			modProc5Mod1 = 0, modProc5Mod2 = 0, modProc5Mod3 = 0, modProc6Mod1 = 0, modProc6Mod2 = 0, modProc6Mod3 = 0,
			modWetDryMix = 0;

			var outProc1Mod1, outProc1Mod2, outProc1Mod3, outProc2Mod1, outProc2Mod2, outProc2Mod3;
			var outProc3Mod1, outProc3Mod2, outProc3Mod3, outProc4Mod1, outProc4Mod2, outProc4Mod3;
			var outProc5Mod1, outProc5Mod2, outProc5Mod3, outProc6Mod1, outProc6Mod2, outProc6Mod3;
			var outFunction1, outFunction2, outFunction3, outFunction4, outFunction5, outFunction6;
			var input, inputSC, chain, sideChain, fftSize, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outProc1Mod1 = proc1Mod1Min + ((proc1Mod1Max - proc1Mod1Min) * (proc1Mod1 + modProc1Mod1).max(0).min(1));
			outProc1Mod2 = proc1Mod2Min + ((proc1Mod2Max - proc1Mod2Min) * (proc1Mod2 + modProc1Mod2).max(0).min(1));
			outProc1Mod3 = proc1Mod3Min + ((proc1Mod3Max - proc1Mod3Min) * (proc1Mod3 + modProc1Mod3).max(0).min(1));
			outProc2Mod1 = proc2Mod1Min + ((proc2Mod1Max - proc2Mod1Min) * (proc2Mod1 + modProc2Mod1).max(0).min(1));
			outProc2Mod2 = proc2Mod2Min + ((proc2Mod2Max - proc2Mod2Min) * (proc2Mod2 + modProc2Mod2).max(0).min(1));
			outProc2Mod3 = proc2Mod3Min + ((proc2Mod3Max - proc2Mod3Min) * (proc2Mod3 + modProc2Mod3).max(0).min(1));
			outProc3Mod1 = proc3Mod1Min + ((proc3Mod1Max - proc3Mod1Min) * (proc3Mod1 + modProc3Mod1).max(0).min(1));
			outProc3Mod2 = proc3Mod2Min + ((proc3Mod2Max - proc3Mod2Min) * (proc3Mod2 + modProc3Mod2).max(0).min(1));
			outProc3Mod3 = proc3Mod3Min + ((proc3Mod3Max - proc3Mod3Min) * (proc3Mod3 + modProc3Mod3).max(0).min(1));
			outProc4Mod1 = proc4Mod1Min + ((proc4Mod1Max - proc4Mod1Min) * (proc4Mod1 + modProc4Mod1).max(0).min(1));
			outProc4Mod2 = proc4Mod2Min + ((proc4Mod2Max - proc4Mod2Min) * (proc4Mod2 + modProc4Mod2).max(0).min(1));
			outProc4Mod3 = proc4Mod3Min + ((proc4Mod3Max - proc4Mod3Min) * (proc4Mod3 + modProc4Mod3).max(0).min(1));
			outProc5Mod1 = proc5Mod1Min + ((proc5Mod1Max - proc5Mod1Min) * (proc5Mod1 + modProc5Mod1).max(0).min(1));
			outProc5Mod2 = proc5Mod2Min + ((proc5Mod2Max - proc5Mod2Min) * (proc5Mod2 + modProc5Mod2).max(0).min(1));
			outProc5Mod3 = proc5Mod3Min + ((proc5Mod3Max - proc5Mod3Min) * (proc5Mod3 + modProc5Mod3).max(0).min(1));
			outProc6Mod1 = proc6Mod1Min + ((proc6Mod1Max - proc6Mod1Min) * (proc6Mod1 + modProc6Mod1).max(0).min(1));
			outProc6Mod2 = proc6Mod2Min + ((proc6Mod2Max - proc6Mod2Min) * (proc6Mod2 + modProc6Mod2).max(0).min(1));
			outProc6Mod3 = proc6Mod3Min + ((proc6Mod3Max - proc6Mod3Min) * (proc6Mod3 + modProc6Mod3).max(0).min(1));

			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			inputSC = TXClean.ar(startEnv * InFeedback.ar(inSideChain,1));
			fftSize = this.getSynthOption(6);
			chain = FFT(LocalBuf(fftSize), input);
			sideChain = FFT(LocalBuf(fftSize), inputSC);

			outFunction1 = this.getSynthOption(0);
			outFunction2 = this.getSynthOption(1);
			outFunction3 = this.getSynthOption(2);
			outFunction4 = this.getSynthOption(3);
			outFunction5 = this.getSynthOption(4);
			outFunction6 = this.getSynthOption(5);

			chain = outFunction1.value(chain, sideChain, fftSize, outProc1Mod1, outProc1Mod2, outProc1Mod3);
			chain = outFunction2.value(chain, sideChain, fftSize, outProc2Mod1, outProc2Mod2, outProc2Mod3);
			chain = outFunction3.value(chain, sideChain, fftSize, outProc3Mod1, outProc3Mod2, outProc3Mod3);
			chain = outFunction4.value(chain, sideChain, fftSize, outProc4Mod1, outProc4Mod2, outProc4Mod3);
			chain = outFunction5.value(chain, sideChain, fftSize, outProc5Mod1, outProc5Mod2, outProc5Mod3);
			chain = outFunction6.value(chain, sideChain, fftSize, outProc6Mod1, outProc6Mod2, outProc6Mod3);

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = (IFFT(chain) * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		// //	make buffers, load the synthdef and create the synth
		// this.makeBuffersAndSynth(arrBufferSpecs);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Settings", {displayOption = "showSettings";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showSettings")],
			["Spacer", 3],
			["ActionButton", "Process 1", {displayOption = "showProcess1";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess1")],
			["Spacer", 3],
			["ActionButton", "Process 2", {displayOption = "showProcess2";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess2")],
			["Spacer", 3],
			["ActionButton", "Process 3", {displayOption = "showProcess3";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess3")],
			["Spacer", 3],
			["ActionButton", "Process 4", {displayOption = "showProcess4";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess4")],
			["Spacer", 3],
			["ActionButton", "Process 5", {displayOption = "showProcess5";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess5")],
			["Spacer", 3],
			["ActionButton", "Process 6", {displayOption = "showProcess6";
				this.buildGuiSpecArray; system.showView;}, 95,
			TXColor.white, this.getButtonColour(displayOption == "showProcess6")],
			["Spacer", 3],
			["DividingLine"],
			["SpacerLine", 6],
		];

		if (displayOption == "showSettings", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "Settings for Spectral Processing", 220],
				["Spacer", 3],
				["ActionButton", "Default 1",
					{arrOptions[6] = 2; arrOptions[7] = 0; arrOptions[8] = 0;
						this.rebuildSynth; system.flagGuiIfModDisplay(this);},
					80, TXColor.white, TXColor.sysGuiCol1],
				["Spacer", 3],
				["ActionButton", "Default 2",
					{arrOptions[6] = 2; arrOptions[7] = 1; arrOptions[8] = 1;
						this.rebuildSynth; system.flagGuiIfModDisplay(this);}, 80,
					TXColor.white, TXColor.sysGuiCol1],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "FFT size", arrOptionData, 6],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "FFT window", arrOptionData, 7],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "FFT hop", arrOptionData, 8],
				["SpacerLine", 20],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showProcess1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 1", arrOptionData, 0, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(0).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc1Mod1", "proc1Mod1Min", "proc1Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc1Mod2", "proc1Mod2Min", "proc1Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc1Mod3", "proc1Mod3Min", "proc1Mod3Max"],
			];
		});
		if (displayOption == "showProcess2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 2", arrOptionData, 1, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(1).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc2Mod1", "proc2Mod1Min", "proc2Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc2Mod2", "proc2Mod2Min", "proc2Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc2Mod3", "proc2Mod3Min", "proc2Mod3Max"],
			];
		});
		if (displayOption == "showProcess3", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 3", arrOptionData, 2, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(2).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc3Mod1", "proc3Mod1Min", "proc3Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc3Mod2", "proc3Mod2Min", "proc3Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc3Mod3", "proc3Mod3Min", "proc3Mod3Max"],
			];
		});
		if (displayOption == "showProcess4", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 4", arrOptionData, 3, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(3).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc4Mod1", "proc4Mod1Min", "proc4Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc4Mod2", "proc4Mod2Min", "proc4Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc4Mod3", "proc4Mod3Min", "proc4Mod3Max"],
			];
		});
		if (displayOption == "showProcess5", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 5", arrOptionData, 4, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(4).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc5Mod1", "proc5Mod1Min", "proc5Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc5Mod2", "proc5Mod2Min", "proc5Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc5Mod3", "proc5Mod3Min", "proc5Mod3Max"],
			];
		});
		if (displayOption == "showProcess6", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Process 6", arrOptionData, 5, 450, 110, {system.flagGuiUpd}],
				["TextViewDisplay", {TXPVProcess.arrDescriptions.at(arrOptions.at(5).asInteger);}, nil, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "proc6Mod1", "proc6Mod1Min", "proc6Mod1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "proc6Mod2", "proc6Mod2Min", "proc6Mod2Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "proc6Mod3", "proc6Mod3Min", "proc6Mod3Max"],
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

}
