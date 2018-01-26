// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXModSequencer : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var midiNoteOnFunc;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.guiWidth = 960;
		classData.arrInstances = [];
		classData.defaultName = "Mod Sequencer";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["BPM", 1, "modClockBPM", 0],
			["Multiply BPM", 1, "modMultiplyBPM", 0],
			["Jitter", 1, "modJitter", 0],
			["Step order", 1, "modStepOrder", 0],
			["Play clock", 1, "modPlay", 0],
			["Trigger pulse", 1, "clockTrig", nil],
			["Restart", 1, "restartTrig", nil],
			["Start step", 1, "modStartStep", nil],
			["Seq length", 1, "modSeqLength", nil],
			["Select index", 1, "modSelectIndex", nil],
			["Loop", 1, "modLoop", 0],

			["Step 1 Freq", 1, "modStep1Freq", 0 ],
			["Step 1 Amp", 1, "modStep1Amp", 0 ],
			["Step 1 CV1", 1, "modStep1CV1", 0 ],
			["Step 1 CV2", 1, "modStep1CV2", 0 ],
			["Step 1 Beats/CV3", 1, "modStep1BeatsCV3", 0 ],
			["Step 2 Freq", 1, "modStep2Freq", 0 ],
			["Step 2 Amp", 1, "modStep2Amp", 0 ],
			["Step 2 CV1", 1, "modStep2CV1", 0 ],
			["Step 2 CV2", 1, "modStep2CV2", 0 ],
			["Step 2 Beats/CV3", 1, "modStep2BeatsCV3", 0 ],
			["Step 3 Freq", 1, "modStep3Freq", 0 ],
			["Step 3 Amp", 1, "modStep3Amp", 0 ],
			["Step 3 CV1", 1, "modStep3CV1", 0 ],
			["Step 3 CV2", 1, "modStep3CV2", 0 ],
			["Step 3 Beats/CV3", 1, "modStep3BeatsCV3", 0 ],
			["Step 4 Freq", 1, "modStep4Freq", 0 ],
			["Step 4 Amp", 1, "modStep4Amp", 0 ],
			["Step 4 CV1", 1, "modStep4CV1", 0 ],
			["Step 4 CV2", 1, "modStep4CV2", 0 ],
			["Step 4 Beats/CV3", 1, "modStep4BeatsCV3", 0 ],
			["Step 5 Freq", 1, "modStep5Freq", 0 ],
			["Step 5 Amp", 1, "modStep5Amp", 0 ],
			["Step 5 CV1", 1, "modStep5CV1", 0 ],
			["Step 5 CV2", 1, "modStep5CV2", 0 ],
			["Step 5 Beats/CV3", 1, "modStep5BeatsCV3", 0 ],
			["Step 6 Freq", 1, "modStep6Freq", 0 ],
			["Step 6 Amp", 1, "modStep6Amp", 0 ],
			["Step 6 CV1", 1, "modStep6CV1", 0 ],
			["Step 6 CV2", 1, "modStep6CV2", 0 ],
			["Step 6 Beats/CV3", 1, "modStep6BeatsCV3", 0 ],
			["Step 7 Freq", 1, "modStep7Freq", 0 ],
			["Step 7 Amp", 1, "modStep7Amp", 0 ],
			["Step 7 CV1", 1, "modStep7CV1", 0 ],
			["Step 7 CV2", 1, "modStep7CV2", 0 ],
			["Step 7 Beats/CV3", 1, "modStep7BeatsCV3", 0 ],
			["Step 8 Freq", 1, "modStep8Freq", 0 ],
			["Step 8 Amp", 1, "modStep8Amp", 0 ],
			["Step 8 CV1", 1, "modStep8CV1", 0 ],
			["Step 8 CV2", 1, "modStep8CV2", 0 ],
			["Step 8 Beats/CV3", 1, "modStep8BeatsCV3", 0 ],
			["Step 9 Freq", 1, "modStep9Freq", 0 ],
			["Step 9 Amp", 1, "modStep9Amp", 0 ],
			["Step 9 CV1", 1, "modStep9CV1", 0 ],
			["Step 9 CV2", 1, "modStep9CV2", 0 ],
			["Step 9 Beats/CV3", 1, "modStep9BeatsCV3", 0 ],
			["Step 10 Freq", 1, "modStep10Freq", 0 ],
			["Step 10 Amp", 1, "modStep10Amp", 0 ],
			["Step 10 CV1", 1, "modStep10CV1", 0 ],
			["Step 10 CV2", 1, "modStep10CV2", 0 ],
			["Step 10 Beats/CV3", 1, "modStep10BeatsCV3", 0 ],
			["Step 11 Freq", 1, "modStep11Freq", 0 ],
			["Step 11 Amp", 1, "modStep11Amp", 0 ],
			["Step 11 CV1", 1, "modStep11CV1", 0 ],
			["Step 11 CV2", 1, "modStep11CV2", 0 ],
			["Step 11 Beats/CV3", 1, "modStep11BeatsCV3", 0 ],
			["Step 12 Freq", 1, "modStep12Freq", 0 ],
			["Step 12 Amp", 1, "modStep12Amp", 0 ],
			["Step 12 CV1", 1, "modStep12CV1", 0 ],
			["Step 12 CV2", 1, "modStep12CV2", 0 ],
			["Step 12 Beats/CV3", 1, "modStep12BeatsCV3", 0 ],
			["Step 13 Freq", 1, "modStep13Freq", 0 ],
			["Step 13 Amp", 1, "modStep13Amp", 0 ],
			["Step 13 CV1", 1, "modStep13CV1", 0 ],
			["Step 13 CV2", 1, "modStep13CV2", 0 ],
			["Step 13 Beats/CV3", 1, "modStep13BeatsCV3", 0 ],
			["Step 14 Freq", 1, "modStep14Freq", 0 ],
			["Step 14 Amp", 1, "modStep14Amp", 0 ],
			["Step 14 CV1", 1, "modStep14CV1", 0 ],
			["Step 14 CV2", 1, "modStep14CV2", 0 ],
			["Step 14 Beats/CV3", 1, "modStep14BeatsCV3", 0 ],
			["Step 15 Freq", 1, "modStep15Freq", 0 ],
			["Step 15 Amp", 1, "modStep15Amp", 0 ],
			["Step 15 CV1", 1, "modStep15CV1", 0 ],
			["Step 15 CV2", 1, "modStep15CV2", 0 ],
			["Step 15 Beats/CV3", 1, "modStep15BeatsCV3", 0 ],
			["Step 16 Freq", 1, "modStep16Freq", 0 ],
			["Step 16 Amp", 1, "modStep16Amp", 0 ],
			["Step 16 CV1", 1, "modStep16CV1", 0 ],
			["Step 16 CV2", 1, "modStep16CV2", 0 ],
			["Step 16 Beats/CV3", 1, "modStep16BeatsCV3", 0 ],
		];
		classData.noOutChannels = 7;
		classData.arrOutBusSpecs = [
			["Freq", [0]],
			["Amp", [1]],
			["CV1", [2]],
			["CV2", [3]],
			["CV3", [4]],
			["Clock", [6]],  // 5+6 swapped for bug fix
			["EndOfSeq", [5]],
			["Gate", [7]],
		];
		classData.stepBeatsQuantOptions = [
			["0 (off)", 0],
			["1 beat", 1],
			["1/2 beat", 1/2],
			["1/3 beat", 1/3],
			["1/4 beat", 1/4],
			["1/5 beat", 1/5],
			["1/6 beat", 1/6],
			["1/7 beat", 1/7],
			["1/8 beat", 1/8],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdBeatsInitVal = ControlSpec(0.25, 4).unmap(1);
		//	set  class specific instance variables
		displayOption = "showParameters";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["clockTrig", 0, 0],
			["t_userClockTrig", 0, \tr],
			["restartTrig", 0, 0],
			["t_userRestartTrig", 0, \tr],
			["clockBPM", 0.3333333], 		// default is set for 120 bpm
			["clockBPMMin", 30],
			["clockBPMMax", 300],
			["multiplyBPM", 0],
			["multiplyBPMMin", 1],
			["multiplyBPMMax", 16],
			["jitter", 0, 0],
			["jitterMin", 0],
			["jitterMax", 1],
			["stepOrder", 1, 0],
			["play", 1, 0, 0],
			["startStep", 0, 0],
			["startStepMin", 1, 0],
			["startStepMax", 16, 0],
			["seqLength", 1, 0],
			["seqLengthMin", 1, 0],
			["seqLengthMax", 16, 0],
			["selectIndex", 0, 0],
			["selectIndexMin", 1, 0],
			["selectIndexMax", 16, 0],
			["loop", 1, 0],
			["freqQuantise", 0, 0],
			["freqMin", 0, 0],
			["freqMax", 127, 0],
			["stepBeatsMin", 0.25, 0],
			["stepBeatsMax", 4, 0],
			["stepBeatsQuantIndex", 0, 0],

			["step1Freq", 0.5, 0 ],
			["step1Amp", 0.5, 0 ],
			["step1CV1", 0, 0 ],
			["step1CV2", 0, 0 ],
			["step1BeatsCV3", holdBeatsInitVal, 0 ],
			["step2Freq", 0.5, 0 ],
			["step2Amp", 0.5, 0 ],
			["step2CV1", 0, 0 ],
			["step2CV2", 0, 0 ],
			["step2BeatsCV3", holdBeatsInitVal, 0 ],
			["step3Freq", 0.5, 0 ],
			["step3Amp", 0.5, 0 ],
			["step3CV1", 0, 0 ],
			["step3CV2", 0, 0 ],
			["step3BeatsCV3", holdBeatsInitVal, 0 ],
			["step4Freq", 0.5, 0 ],
			["step4Amp", 0.5, 0 ],
			["step4CV1", 0, 0 ],
			["step4CV2", 0, 0 ],
			["step4BeatsCV3", holdBeatsInitVal, 0 ],
			["step5Freq", 0.5, 0 ],
			["step5Amp", 0.5, 0 ],
			["step5CV1", 0, 0 ],
			["step5CV2", 0, 0 ],
			["step5BeatsCV3", holdBeatsInitVal, 0 ],
			["step6Freq", 0.5, 0 ],
			["step6Amp", 0.5, 0 ],
			["step6CV1", 0, 0 ],
			["step6CV2", 0, 0 ],
			["step6BeatsCV3", holdBeatsInitVal, 0 ],
			["step7Freq", 0.5, 0 ],
			["step7Amp", 0.5, 0 ],
			["step7CV1", 0, 0 ],
			["step7CV2", 0, 0 ],
			["step7BeatsCV3", holdBeatsInitVal, 0 ],
			["step8Freq", 0.5, 0 ],
			["step8Amp", 0.5, 0 ],
			["step8CV1", 0, 0 ],
			["step8CV2", 0, 0 ],
			["step8BeatsCV3", holdBeatsInitVal, 0 ],
			["step9Freq", 0.5, 0 ],
			["step9Amp", 0.5, 0 ],
			["step9CV1", 0, 0 ],
			["step9CV2", 0, 0 ],
			["step9BeatsCV3", holdBeatsInitVal, 0 ],
			["step10Freq", 0.5, 0 ],
			["step10Amp", 0.5, 0 ],
			["step10CV1", 0, 0 ],
			["step10CV2", 0, 0 ],
			["step10BeatsCV3", holdBeatsInitVal, 0 ],
			["step11Freq", 0.5, 0 ],
			["step11Amp", 0.5, 0 ],
			["step11CV1", 0, 0 ],
			["step11CV2", 0, 0 ],
			["step11BeatsCV3", holdBeatsInitVal, 0 ],
			["step12Freq", 0.5, 0 ],
			["step12Amp", 0.5, 0 ],
			["step12CV1", 0, 0 ],
			["step12CV2", 0, 0 ],
			["step12BeatsCV3", holdBeatsInitVal, 0 ],
			["step13Freq", 0.5, 0 ],
			["step13Amp", 0.5, 0 ],
			["step13CV1", 0, 0 ],
			["step13CV2", 0, 0 ],
			["step13BeatsCV3", holdBeatsInitVal, 0 ],
			["step14Freq", 0.5, 0 ],
			["step14Amp", 0.5, 0 ],
			["step14CV1", 0, 0 ],
			["step14CV2", 0, 0 ],
			["step14BeatsCV3", holdBeatsInitVal, 0 ],
			["step15Freq", 0.5, 0 ],
			["step15Amp", 0.5, 0 ],
			["step15CV1", 0, 0 ],
			["step15CV2", 0, 0 ],
			["step15BeatsCV3", holdBeatsInitVal, 0 ],
			["step16Freq", 0.5, 0 ],
			["step16Amp", 0.5, 0 ],
			["step16CV1", 0, 0 ],
			["step16CV2", 0, 0 ],
			["step16BeatsCV3", holdBeatsInitVal, 0 ],

			["modClockBPM", 0, 0],
			["modMultiplyBPM", 0, 0],
			["modJitter", 0, 0],
			["modStepOrder", 0, 0],
			["modPlay", 0, 0],
			["modStartStep", 0, 0],
			["modSeqLength", 0, 0],
			["modSelectIndex", 0, 0],
			["modLoop", 0, 0],

			["modStep1Freq", 0, 0 ],
			["modStep1Amp", 0, 0 ],
			["modStep1CV1", 0, 0 ],
			["modStep1CV2", 0, 0 ],
			["modStep1BeatsCV3", 0, 0 ],
			["modStep2Freq", 0, 0 ],
			["modStep2Amp", 0, 0 ],
			["modStep2CV1", 0, 0 ],
			["modStep2CV2", 0, 0 ],
			["modStep2BeatsCV3", 0, 0 ],
			["modStep3Freq", 0, 0 ],
			["modStep3Amp", 0, 0 ],
			["modStep3CV1", 0, 0 ],
			["modStep3CV2", 0, 0 ],
			["modStep3BeatsCV3", 0, 0 ],
			["modStep4Freq", 0, 0 ],
			["modStep4Amp", 0, 0 ],
			["modStep4CV1", 0, 0 ],
			["modStep4CV2", 0, 0 ],
			["modStep4BeatsCV3", 0, 0 ],
			["modStep5Freq", 0, 0 ],
			["modStep5Amp", 0, 0 ],
			["modStep5CV1", 0, 0 ],
			["modStep5CV2", 0, 0 ],
			["modStep5BeatsCV3", 0, 0 ],
			["modStep6Freq", 0, 0 ],
			["modStep6Amp", 0, 0 ],
			["modStep6CV1", 0, 0 ],
			["modStep6CV2", 0, 0 ],
			["modStep6BeatsCV3", 0, 0 ],
			["modStep7Freq", 0, 0 ],
			["modStep7Amp", 0, 0 ],
			["modStep7CV1", 0, 0 ],
			["modStep7CV2", 0, 0 ],
			["modStep7BeatsCV3", 0, 0 ],
			["modStep8Freq", 0, 0 ],
			["modStep8Amp", 0, 0 ],
			["modStep8CV1", 0, 0 ],
			["modStep8CV2", 0, 0 ],
			["modStep8BeatsCV3", 0, 0 ],
			["modStep9Freq", 0, 0 ],
			["modStep9Amp", 0, 0 ],
			["modStep9CV1", 0, 0 ],
			["modStep9CV2", 0, 0 ],
			["modStep9BeatsCV3", 0, 0 ],
			["modStep10Freq", 0, 0 ],
			["modStep10Amp", 0, 0 ],
			["modStep10CV1", 0, 0 ],
			["modStep10CV2", 0, 0 ],
			["modStep10BeatsCV3", 0, 0 ],
			["modStep11Freq", 0, 0 ],
			["modStep11Amp", 0, 0 ],
			["modStep11CV1", 0, 0 ],
			["modStep11CV2", 0, 0 ],
			["modStep11BeatsCV3", 0, 0 ],
			["modStep12Freq", 0, 0 ],
			["modStep12Amp", 0, 0 ],
			["modStep12CV1", 0, 0 ],
			["modStep12CV2", 0, 0 ],
			["modStep12BeatsCV3", 0, 0 ],
			["modStep13Freq", 0, 0 ],
			["modStep13Amp", 0, 0 ],
			["modStep13CV1", 0, 0 ],
			["modStep13CV2", 0, 0 ],
			["modStep13BeatsCV3", 0, 0 ],
			["modStep14Freq", 0, 0 ],
			["modStep14Amp", 0, 0 ],
			["modStep14CV1", 0, 0 ],
			["modStep14CV2", 0, 0 ],
			["modStep14BeatsCV3", 0, 0 ],
			["modStep15Freq", 0, 0 ],
			["modStep15Amp", 0, 0 ],
			["modStep15CV1", 0, 0 ],
			["modStep15CV2", 0, 0 ],
			["modStep15BeatsCV3", 0, 0 ],
			["modStep16Freq", 0, 0 ],
			["modStep16Amp", 0, 0 ],
			["modStep16CV1", 0, 0 ],
			["modStep16CV2", 0, 0 ],
			["modStep16BeatsCV3", 0, 0 ],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience

			["midiLearn", 0],
			["midiLearnStep", 1],
		];

		synthDefFunc = { arg out, clockTrig = 0, t_userClockTrig = 0, restartTrig = 0, t_userRestartTrig = 0,
			clockBPM, clockBPMMin, clockBPMMax, multiplyBPM, multiplyBPMMin, multiplyBPMMax,
			jitter, jitterMin, jitterMax,
			stepOrder, play,
			startStep, startStepMin, startStepMax,
			seqLength, seqLengthMin, seqLengthMax,
			selectIndex, selectIndexMin, selectIndexMax,
			loop, freqQuantise, freqMin, freqMax,
			stepBeatsMin, stepBeatsMax, stepBeatsQuantIndex = 0,

			step1Freq, step1Amp, step1CV1, step1CV2, step1BeatsCV3,
			step2Freq, step2Amp, step2CV1, step2CV2, step2BeatsCV3,
			step3Freq, step3Amp, step3CV1, step3CV2, step3BeatsCV3,
			step4Freq, step4Amp, step4CV1, step4CV2, step4BeatsCV3,
			step5Freq, step5Amp, step5CV1, step5CV2, step5BeatsCV3,
			step6Freq, step6Amp, step6CV1, step6CV2, step6BeatsCV3,
			step7Freq, step7Amp, step7CV1, step7CV2, step7BeatsCV3,
			step8Freq, step8Amp, step8CV1, step8CV2, step8BeatsCV3,
			step9Freq, step9Amp, step9CV1, step9CV2, step9BeatsCV3,
			step10Freq, step10Amp, step10CV1, step10CV2, step10BeatsCV3,
			step11Freq, step11Amp, step11CV1, step11CV2, step11BeatsCV3,
			step12Freq, step12Amp, step12CV1, step12CV2, step12BeatsCV3,
			step13Freq, step13Amp, step13CV1, step13CV2, step13BeatsCV3,
			step14Freq, step14Amp, step14CV1, step14CV2, step14BeatsCV3,
			step15Freq, step15Amp, step15CV1, step15CV2, step15BeatsCV3,
			step16Freq, step16Amp, step16CV1, step16CV2, step16BeatsCV3,

			modClockBPM = 0, modMultiplyBPM = 0, modJitter = 0, modStepOrder = 0,
			modPlay = 0, modStartStep = 0, modSeqLength = 0, modSelectIndex = 0, modLoop = 0,

			modStep1Freq = 0, modStep1Amp = 0, modStep1CV1 = 0, modStep1CV2 = 0, modStep1BeatsCV3 = 0,
			modStep2Freq = 0, modStep2Amp = 0, modStep2CV1 = 0, modStep2CV2 = 0, modStep2BeatsCV3 = 0,
			modStep3Freq = 0, modStep3Amp = 0, modStep3CV1 = 0, modStep3CV2 = 0, modStep3BeatsCV3 = 0,
			modStep4Freq = 0, modStep4Amp = 0, modStep4CV1 = 0, modStep4CV2 = 0, modStep4BeatsCV3 = 0,
			modStep5Freq = 0, modStep5Amp = 0, modStep5CV1 = 0, modStep5CV2 = 0, modStep5BeatsCV3 = 0,
			modStep6Freq = 0, modStep6Amp = 0, modStep6CV1 = 0, modStep6CV2 = 0, modStep6BeatsCV3 = 0,
			modStep7Freq = 0, modStep7Amp = 0, modStep7CV1 = 0, modStep7CV2 = 0, modStep7BeatsCV3 = 0,
			modStep8Freq = 0, modStep8Amp = 0, modStep8CV1 = 0, modStep8CV2 = 0, modStep8BeatsCV3 = 0,
			modStep9Freq = 0, modStep9Amp = 0, modStep9CV1 = 0, modStep9CV2 = 0, modStep9BeatsCV3 = 0,
			modStep10Freq = 0, modStep10Amp = 0, modStep10CV1 = 0, modStep10CV2 = 0, modStep10BeatsCV3 = 0,
			modStep11Freq = 0, modStep11Amp = 0, modStep11CV1 = 0, modStep11CV2 = 0, modStep11BeatsCV3 = 0,
			modStep12Freq = 0, modStep12Amp = 0, modStep12CV1 = 0, modStep12CV2 = 0, modStep12BeatsCV3 = 0,
			modStep13Freq = 0, modStep13Amp = 0, modStep13CV1 = 0, modStep13CV2 = 0, modStep13BeatsCV3 = 0,
			modStep14Freq = 0, modStep14Amp = 0, modStep14CV1 = 0, modStep14CV2 = 0, modStep14BeatsCV3 = 0,
			modStep15Freq = 0, modStep15Amp = 0, modStep15CV1 = 0, modStep15CV2 = 0, modStep15BeatsCV3 = 0,
			modStep16Freq = 0, modStep16Amp = 0, modStep16CV1 = 0, modStep16CV2 = 0, modStep16BeatsCV3 = 0
			;

			var arrStepVals = [
				 step1Freq, step1Amp, step1CV1, step1CV2, step1BeatsCV3,
				 step2Freq, step2Amp, step2CV1, step2CV2, step2BeatsCV3,
				 step3Freq, step3Amp, step3CV1, step3CV2, step3BeatsCV3,
				 step4Freq, step4Amp, step4CV1, step4CV2, step4BeatsCV3,
				 step5Freq, step5Amp, step5CV1, step5CV2, step5BeatsCV3,
				 step6Freq, step6Amp, step6CV1, step6CV2, step6BeatsCV3,
				 step7Freq, step7Amp, step7CV1, step7CV2, step7BeatsCV3,
				 step8Freq, step8Amp, step8CV1, step8CV2, step8BeatsCV3,
				 step9Freq, step9Amp, step9CV1, step9CV2, step9BeatsCV3,
				 step10Freq, step10Amp, step10CV1, step10CV2, step10BeatsCV3,
				 step11Freq, step11Amp, step11CV1, step11CV2, step11BeatsCV3,
				 step12Freq, step12Amp, step12CV1, step12CV2, step12BeatsCV3,
				 step13Freq, step13Amp, step13CV1, step13CV2, step13BeatsCV3,
				 step14Freq, step14Amp, step14CV1, step14CV2, step14BeatsCV3,
				 step15Freq, step15Amp, step15CV1, step15CV2, step15BeatsCV3,
				 step16Freq, step16Amp, step16CV1, step16CV2, step16BeatsCV3,
			];
			var arrModVals = TXClean.kr([
				 modStep1Freq, modStep1Amp, modStep1CV1, modStep1CV2, modStep1BeatsCV3,
				 modStep2Freq, modStep2Amp, modStep2CV1, modStep2CV2, modStep2BeatsCV3,
				 modStep3Freq, modStep3Amp, modStep3CV1, modStep3CV2, modStep3BeatsCV3,
				 modStep4Freq, modStep4Amp, modStep4CV1, modStep4CV2, modStep4BeatsCV3,
				 modStep5Freq, modStep5Amp, modStep5CV1, modStep5CV2, modStep5BeatsCV3,
				 modStep6Freq, modStep6Amp, modStep6CV1, modStep6CV2, modStep6BeatsCV3,
				 modStep7Freq, modStep7Amp, modStep7CV1, modStep7CV2, modStep7BeatsCV3,
				 modStep8Freq, modStep8Amp, modStep8CV1, modStep8CV2, modStep8BeatsCV3,
				 modStep9Freq, modStep9Amp, modStep9CV1, modStep9CV2, modStep9BeatsCV3,
				 modStep10Freq, modStep10Amp, modStep10CV1, modStep10CV2, modStep10BeatsCV3,
				 modStep11Freq, modStep11Amp, modStep11CV1, modStep11CV2, modStep11BeatsCV3,
				 modStep12Freq, modStep12Amp, modStep12CV1, modStep12CV2, modStep12BeatsCV3,
				 modStep13Freq, modStep13Amp, modStep13CV1, modStep13CV2, modStep13BeatsCV3,
				 modStep14Freq, modStep14Amp, modStep14CV1, modStep14CV2, modStep14BeatsCV3,
				 modStep15Freq, modStep15Amp, modStep15CV1, modStep15CV2, modStep15BeatsCV3,
				 modStep16Freq, modStep16Amp, modStep16CV1, modStep16CV2, modStep16BeatsCV3,
			]);
			var startStepSum = (startStep + modStartStep).max(0).min(1).linlin(0, 1, startStepMin, startStepMax).round;
			var seqLengthSum = (seqLength + modSeqLength).max(0).min(1).linlin(0, 1, seqLengthMin, seqLengthMax).round;
			var stepOrderIndex = ((stepOrder - 1) + modStepOrder.linlin(0, 1, 0, 4).round).max(0).min(4);
			var stepOrderTrig = Changed.kr(stepOrder);
			var playSum = (play + modPlay).max(0).min(1).round;
			var playTrig = Trig1.kr(playSum, ControlDur.ir);
			var selectIndexSum =
				(selectIndex + modSelectIndex).max(0).min(1).linlin(0, 1, selectIndexMin, selectIndexMax).round;
			var loopSum = (loop + modLoop).max(0).min(1).round;

			var holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig);
			var holdRestartTrig = TXClean.kr(restartTrig + t_userRestartTrig + playTrig + stepOrderTrig);
			var clockBPMSum = (clockBPM + modClockBPM).max(0).min(1).linlin(0, 1, clockBPMMin, clockBPMMax);
			var multiplyBPMSum = (multiplyBPM + modMultiplyBPM).max(0).min(1)
				.linlin(0, 1, multiplyBPMMin, multiplyBPMMax).round;
			var jitterSum = (jitter + modJitter).max(0).min(1).linlin(0, 1, jitterMin, jitterMax);

			var staticFreq, randLFO, outClockFreq;
			var stepFreqSum, stepAmpSum, stepCV1Sum, stepCV2Sum, stepBeatsCV3Sum;
			var stepFreqQuant, arrOuts, holdClockOut, holdEndOfSeq;
			var internalClock, currStepInd, holdRate, holdPhasor;

			var stepBeatsBuffer = LocalBuf(2, 1).clear;
			var stepBeatsQuantise, freqMinNorm, freqMaxNorm, recip127;

			stepBeatsQuantise = Select.kr(stepBeatsQuantIndex, [0, 1, 1/2, 1/3, 1/4, 1/5, 1/6, 1/7, 1/8]) ;

			// read step beats from buffer
			staticFreq = clockBPMSum *  multiplyBPMSum/ (60 * BufRd.kr(1, stepBeatsBuffer, 0, 1, interpolation: 1).max(0.1));
			randLFO = LFNoise1.kr(staticFreq * (3 + (8 * jitterSum)));
			outClockFreq = staticFreq * (jitterSum * randLFO).linexp(-1, 1, 0.1, 10);
			holdRate = outClockFreq / ControlRate.ir;
			holdPhasor = Phasor.kr(holdClockTrig, holdRate) - 0.02; // neg offset
			internalClock = (Trig1.kr((holdPhasor > 0) * (holdPhasor <= 0.5), ControlDur.ir)).min(playSum);

			// end of seq is at next trigger following last note
			holdEndOfSeq = (Dseries(0, 1, inf) - (seqLengthSum - 1)).clip(0,1).min(1 - loopSum);

			// 1:Forwards, 2:Backwards, 3:Random, 4:Use Select Index
			currStepInd = { // put into function so all copies unique
				Dswitch1(
					[((startStepSum - 1 + (Dseries(0, 1, Dswitch1([seqLengthSum, inf], loopSum))  % seqLengthSum)) % 16).round,
						((seqLengthSum + startStepSum - 1 - (Dseries(0, 1, Dswitch1([seqLengthSum, inf], loopSum)) % seqLengthSum)
							) % 16).round,
						((startStepSum +
							Diwhite(0, (seqLengthSum - 1), Dswitch1([seqLengthSum, inf], loopSum))) % 16).round,
						(selectIndexSum - 1)
					],
					stepOrderIndex
				)
			};
			recip127 = 127.reciprocal;
			freqMinNorm = freqMin * recip127;
			freqMaxNorm = freqMax * recip127;
			// stepFreqSum = (Dswitch1(arrStepVals, (currStepInd * 5))
			// + Dswitch1(arrModVals, (currStepInd * 5)).linlin(0, 1, freqMinNorm, freqMaxNorm)).max(freqMinNorm).min(freqMaxNorm);
			stepFreqSum = (Dswitch1(arrStepVals, (currStepInd * 5)).linlin(freqMinNorm, freqMaxNorm, 0, 1)
				+ Dswitch1(arrModVals, (currStepInd * 5))
			).max(0).min(1).linlin(0, 1, freqMinNorm, freqMaxNorm);
			stepFreqQuant = stepFreqSum.round(freqQuantise * recip127);
			stepAmpSum = (Dswitch1(arrStepVals, (currStepInd * 5) + 1)
				+ Dswitch1(arrModVals, (currStepInd * 5) + 1)).max(0).min(1);
			stepCV1Sum = (Dswitch1(arrStepVals, (currStepInd * 5) + 2)
				+ Dswitch1(arrModVals, (currStepInd * 5) + 2)).max(0).min(1);
			stepCV2Sum = (Dswitch1(arrStepVals, (currStepInd * 5) + 3)
				+ Dswitch1(arrModVals, (currStepInd * 5) + 3)).max(0).min(1);
			stepBeatsCV3Sum = (Dswitch1(arrStepVals, (currStepInd * 5) + 4)
			 	+ Dswitch1(arrModVals, (currStepInd * 5) + 4)).max(0).min(1);

			holdClockOut = (holdClockTrig + internalClock);
			arrOuts = Demand.kr(holdClockOut, holdRestartTrig,
				[
					stepFreqQuant,
					stepAmpSum,
					stepCV1Sum,
					stepCV2Sum,
					stepBeatsCV3Sum,
					holdEndOfSeq
			]);
			// store step beats to buffer
			BufWr.kr(
				[arrOuts[4].linlin(0, 1, stepBeatsMin, stepBeatsMax).round(stepBeatsQuantise).max(stepBeatsQuantise)],
				stepBeatsBuffer, 0, 1
			);
			Out.kr(out, TXClean.kr(arrOuts ++ [holdClockOut, (holdClockOut.min(1 - arrOuts[5]))]));
		};
		// end of synthDefFunc
		guiSpecTitleArray = [
			["TitleBar"],
			["HelpButton"],
			["RunPauseButton"],
			["DeleteModuleButton"],
			["RebuildModuleButton"],
			["HideModuleButton"],
			["NextLine"],
			["ModuleActionPopup", 420],
			["ModuleInfoTxt", 500],
			["SpacerLine", 2],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXCheckBox", "Play", "play"],
			["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "clockBPM", "clockBPMMin", "clockBPMMax"],
			["TXMinMaxSliderSplit", "Multiply BPM", ControlSpec(1, 16, step: 1), "multiplyBPM",
				"multiplyBPMMin", "multiplyBPMMax"],
			["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax"],
			["TXMinMaxSliderSplit", "Start step", ControlSpec(1, 16, step: 1), "startStep", "startStepMin", "startStepMax"],
			["TXMinMaxSliderSplit", "Seq length", ControlSpec(1, 16, step: 1), "seqLength", "seqLengthMin", "seqLengthMax"],
			["EZslider", "Step order", ControlSpec(1, 4, step: 1), "stepOrder", {}, 200],
			["TXMinMaxSliderSplit", "SelectIndex", ControlSpec(1, 16, step: 1), "selectIndex", "selectIndexMin", "selectIndexMax"],
			["TXCheckBox", "Loop", "loop"],
			["ActionButton", "Restart Seq", {this.moduleNode.set("t_userRestartTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol2],
			["TXRangeSlider", "Beats range", ControlSpec(0.1, 64, 'exp'), "stepBeatsMin", "stepBeatsMax"],
			["TXPopupActionPlusMinus", "Beats quantise", classData.stepBeatsQuantOptions.collect({arg item; item[0]}),
				"stepBeatsQuantIndex"],
			["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
			["TXCheckBox", "Freq Quantise", "freqQuantise"],
		] ++
		[ "step1Freq", "step2Freq", "step3Freq", "step4Freq", "step5Freq", "step6Freq", "step7Freq", "step8Freq",
			"step9Freq", "step10Freq", "step11Freq", "step12Freq", "step13Freq", "step14Freq", "step15Freq", "step16Freq"
		].collect({arg item, i;
			var specFunc, getValFunc, setValFunc;
			specFunc = {ControlSpec(this.getSynthArgSpec("freqMin").midicps,
				this.getSynthArgSpec("freqMax").midicps, 'exp')};
			getValFunc = {
				var spec = ControlSpec(this.getSynthArgSpec("freqMin"), this.getSynthArgSpec("freqMax"));
				var holdVal = this.getSynthArgSpec(item);
				(spec.constrain(holdVal * 127)).midicps;
			};
			setValFunc = {arg argVal;
				var holdVal = argVal.cpsmidi / 127;
				this.setSynthValue(item, holdVal);
			};
			/*
			Note valueActionNumber is used here to get specific behaviour for freqs
			*/
			["valueActionNumber", "Set " ++ item, [specFunc], getValFunc, setValFunc]
		})
		++
		[ "step1Amp", "step2Amp", "step3Amp", "step4Amp", "step5Amp", "step6Amp", "step7Amp", "step8Amp",
			"step9Amp", "step10Amp", "step11Amp", "step12Amp", "step13Amp", "step14Amp", "step15Amp", "step16Amp",
			"step1CV1", "step2CV1", "step3CV1", "step4CV1", "step5CV1", "step6CV1", "step7CV1", "step8CV1",
			"step9CV1", "step10CV1", "step11CV1", "step12CV1", "step13CV1", "step14CV1", "step15CV1", "step16CV1",
			"step1CV2", "step2CV2", "step3CV2", "step4CV2", "step5CV2", "step6CV2", "step7CV2", "step8CV2",
			"step9CV2", "step10CV2", "step11CV2", "step12CV2", "step13CV2", "step14CV2", "step15CV2", "step16CV2",
		].collect({arg item, i;
			["EZslider", item, ControlSpec(0, 1), item]
		})
		++ [ "step1BeatsCV3", "step2BeatsCV3", "step3BeatsCV3", "step4BeatsCV3", "step5BeatsCV3",
			"step6BeatsCV3", "step7BeatsCV3", "step8BeatsCV3", "step9BeatsCV3", "step10BeatsCV3",
			"step11BeatsCV3", "step12BeatsCV3", "step13BeatsCV3", "step14BeatsCV3", "step15BeatsCV3", "step16BeatsCV3",
		].collect({arg item, i;
			["EZsliderUnmapped", item,
				{ControlSpec(this.getSynthArgSpec("stepBeatsMin"), this.getSynthArgSpec("stepBeatsMax"),
					step: classData.stepBeatsQuantOptions[this.getSynthArgSpec("stepBeatsQuantIndex")][1])},
				item]
		})
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdFreqMin = this.getSynthArgSpec("freqMin");
		var holdFreqMax = this.getSynthArgSpec("freqMax");
		var holdStepBeatsMin = this.getSynthArgSpec("stepBeatsMin");
		var holdStepBeatsMax = this.getSynthArgSpec("stepBeatsMax");
		var holdStepBeatsQuantIndex = this.getSynthArgSpec("stepBeatsQuantIndex");

		guiSpecArray = [
			["ActionButton", "Parameters", {displayOption = "showParameters";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			//["Spacer", 10],
			["ActionButton", "Steps", {displayOption = "showSteps";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showSteps")],
			//["Spacer", 10],
			["ActionButton", "Clock", {displayOption = "showClock";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showClock")],
			["Spacer", 120],
			["TXCheckBox", "Play Clock", "play", {}, 120],
			//["Spacer", 10],
			["ActionButton", "Restart Sequence", {this.moduleNode.set("t_userRestartTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			//["Spacer", 10],
			["ActionButton", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showParameters", {
			guiSpecArray = guiSpecArray ++[
				["NextLine"],
				["EZslider", "Step order", ControlSpec(1, 4, step: 1), "stepOrder", {}, 240],
				["TextBarLeft", " 1:Forwards, 2:Backwards, 3:Random, 4:Use Select Index", 320],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Select Index", ControlSpec(1, 16, step: 1),
					"selectIndex", "selectIndexMin", "selectIndexMax", nil, nil, nil, nil, 600],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Start step", ControlSpec(1, 16, step: 1),
					"startStep", "startStepMin", "startStepMax", nil, nil, nil, nil, 600],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Seq length", ControlSpec(1, 16, step: 1),
					"seqLength", "seqLengthMin", "seqLengthMax", nil, nil, nil, nil, 600],
				["SpacerLine", 4],
				["TXCheckBox", "Loop", "loop"],
				["TextBarLeft", " Loop changes will only take effect when seq is restarted", 330],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true, 600],
				["SpacerLine", 2],
				["TXCheckBox", "Freq Quantise", "freqQuantise"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXRangeSlider", "Beats range", ControlSpec(0.1, 64, 'exp'), "stepBeatsMin", "stepBeatsMax",
					nil, nil, 600],
				["SpacerLine", 2],
				["TXPopupActionPlusMinus", "Beats quantise",
					classData.stepBeatsQuantOptions.collect({arg item; item[0]}),
					"stepBeatsQuantIndex",
					{arg view; this.setSynthValue("stepBeatsQuantIndex", view.value);},
					260
				],
			];
		});
		if (displayOption == "showSteps", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "Steps", 80],
				["TextBar", "Step 1", 50],
				["TextBar", "Step 2", 50],
				["TextBar", "Step 3", 50],
				["TextBar", "Step 4", 50],
				["TextBar", "Step 5", 50],
				["TextBar", "Step 6", 50],
				["TextBar", "Step 7", 50],
				["TextBar", "Step 8", 50],
				["TextBar", "Step 9", 50],
				["TextBar", "Step 10", 50],
				["TextBar", "Step 11", 50],
				["TextBar", "Step 12", 50],
				["TextBar", "Step 13", 50],
				["TextBar", "Step 14", 50],
				["TextBar", "Step 15", 50],
				["TextBar", "Step 16", 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["step1Freq", "step2Freq", "step3Freq", "step4Freq", "step5Freq", "step6Freq",
					"step7Freq", "step8Freq", "step9Freq", "step10Freq", "step11Freq", "step12Freq",
					"step13Freq", "step14Freq", "step15Freq", "step16Freq" ]);}, 36],
				["TXMultiKnobMidiNote", "Freq: midi note", ["step1Freq", "step2Freq", "step3Freq", "step4Freq", "step5Freq", "step6Freq",
					"step7Freq", "step8Freq", "step9Freq", "step10Freq", "step11Freq", "step12Freq",
					"step13Freq", "step14Freq", "step15Freq", "step16Freq" ], 16, ControlSpec(holdFreqMin / 127, holdFreqMax / 127)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["step1Amp", "step2Amp", "step3Amp", "step4Amp", "step5Amp", "step6Amp",
					"step7Amp", "step8Amp", "step9Amp", "step10Amp", "step11Amp", "step12Amp",
					"step13Amp", "step14Amp", "step15Amp", "step16Amp" ]);}, 36],
				["TXMultiKnobNo", "Amp", ["step1Amp", "step2Amp", "step3Amp", "step4Amp", "step5Amp", "step6Amp",
					"step7Amp", "step8Amp", "step9Amp", "step10Amp", "step11Amp", "step12Amp",
					"step13Amp", "step14Amp", "step15Amp", "step16Amp" ], 16, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["step1CV1", "step2CV1", "step3CV1", "step4CV1", "step5CV1", "step6CV1",
					"step7CV1", "step8CV1", "step9CV1", "step10CV1", "step11CV1", "step12CV1",
					"step13CV1", "step14CV1", "step15CV1", "step16CV1" ]);}, 36],
				["TXMultiKnobNo", "CV1", ["step1CV1", "step2CV1", "step3CV1", "step4CV1", "step5CV1", "step6CV1",
					"step7CV1", "step8CV1", "step9CV1", "step10CV1", "step11CV1", "step12CV1",
					"step13CV1", "step14CV1", "step15CV1", "step16CV1" ], 16, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["step1CV2", "step2CV2", "step3CV2", "step4CV2", "step5CV2", "step6CV2",
					"step7CV2", "step8CV2", "step9CV2", "step10CV2", "step11CV2", "step12CV2",
					"step13CV2", "step14CV2", "step15CV2", "step16CV2" ]);}, 36],
				["TXMultiKnobNo", "CV2", ["step1CV2", "step2CV2", "step3CV2", "step4CV2", "step5CV2", "step6CV2",
					"step7CV2", "step8CV2", "step9CV2", "step10CV2", "step11CV2", "step12CV2",
					"step13CV2", "step14CV2", "step15CV2", "step16CV2" ], 16, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["step1BeatsCV3", "step2BeatsCV3", "step3BeatsCV3", "step4BeatsCV3",
					"step5BeatsCV3", "step6BeatsCV3", "step7BeatsCV3", "step8BeatsCV3", "step9BeatsCV3", "step10BeatsCV3",
					"step11BeatsCV3", "step12BeatsCV3", "step13BeatsCV3", "step14BeatsCV3", "step15BeatsCV3",
					"step16BeatsCV3" ]);}, 36],
				["TXMultiKnobNoUnmap", "Beats/ CV3", ["step1BeatsCV3", "step2BeatsCV3", "step3BeatsCV3", "step4BeatsCV3",
					"step5BeatsCV3", "step6BeatsCV3", "step7BeatsCV3", "step8BeatsCV3", "step9BeatsCV3", "step10BeatsCV3",
					"step11BeatsCV3", "step12BeatsCV3", "step13BeatsCV3", "step14BeatsCV3", "step15BeatsCV3",
					"step16BeatsCV3" ], 16, ControlSpec(holdStepBeatsMin, holdStepBeatsMax,
					step: classData.stepBeatsQuantOptions[holdStepBeatsQuantIndex][1])],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 2],
				["TextBar", "MIDI Learn", 80, nil, nil, nil, \right],
				["TXCheckBox", "MIDI learn notes & amps (velocities) ", "midiLearn", {arg view; this.setMidiLearnFunc(view.value);}, 240],
				["EZNumber", "Next Step", ControlSpec(1, 16, step: 1), "midiLearnStep"],
				["ActionButton", "Set Next Step: 1", {this.setSynthArgSpec("midiLearnStep", 1); system.showView;}, 100],
			];
		});
		if (displayOption == "showClock", {
			guiSpecArray = guiSpecArray ++[
				["NextLine"],
				["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "clockBPM", "clockBPMMin", "clockBPMMax",
					nil, nil, nil, nil, 600],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Multiply BPM", ControlSpec(1, 16, step: 1), "multiplyBPM",
					"multiplyBPMMin", "multiplyBPMMax", nil, nil, nil, nil, 600],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax",
					nil, nil, nil, nil, 600],
				["SpacerLine", 10],
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

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

	setMidiLearnFunc { arg argSwitch = 0;
		var arrFreqStrings = ["step1Freq", "step2Freq", "step3Freq", "step4Freq", "step5Freq", "step6Freq",
					"step7Freq", "step8Freq", "step9Freq", "step10Freq", "step11Freq", "step12Freq",
					"step13Freq", "step14Freq", "step15Freq", "step16Freq" ];
		var arrAmpStrings = ["step1Amp", "step2Amp", "step3Amp", "step4Amp", "step5Amp", "step6Amp",
					"step7Amp", "step8Amp", "step9Amp", "step10Amp", "step11Amp", "step12Amp",
					"step13Amp", "step14Amp", "step15Amp", "step16Amp" ];
		// stop any old funcs
		this.stopMidiLearn;
		// if requested start midi
		if (argSwitch == 1, {
			midiNoteOnFunc = MIDIFunc.noteOn({ |vel, num, chan|
				var stepNo;
				if (vel > 0, {
					stepNo = this.getSynthArgSpec("midiLearnStep");
					// store freq and amp
					this.setSynthValue(arrFreqStrings[stepNo - 1], num / 127);
					this.setSynthValue(arrAmpStrings[stepNo - 1], vel / 127);
					// go to next step
					this.setSynthArgSpec("midiLearnStep", stepNo + 1);
					if ((stepNo + 1) > 16, {
						this.stopMidiLearn;
						this.setSynthArgSpec("midiLearn", 0);
						this.setSynthArgSpec("midiLearnStep", 1);
					});
					// refresh view
					system.showView;
				});
			});
		}, {
			// refresh view
			system.showViewIfModDisplay(this);
		});
	}

	stopMidiLearn {
		// remove any old funcs
		if (midiNoteOnFunc.notNil, {
			midiNoteOnFunc.free;
			midiNoteOnFunc = nil;
		});
	}

	deleteModuleExtraActions { // override base module
		this.stopMidiLearn;
	}

}

