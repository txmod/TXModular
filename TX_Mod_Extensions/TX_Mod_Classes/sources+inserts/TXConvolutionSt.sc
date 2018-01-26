// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXConvolutionSt : TXModuleBase {

	classvar <>classData;

	var <>sampleNo = 0;
	var <>bankNo = 0;
	var <>sampleData;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var fftsize = 2048;	// could also use 4096

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Convolution St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Input Level", 1, "modInputLevel", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [
			["bufnumSampleL", 2048, 1],
			["bufnumSampleR", 2048, 1],
			["bufnumSpectrumL", 2048, 1],
			["bufnumSpectrumR", 2048, 1],
			["bufnumDelayL", 2*defSampleRate, 1],
			["bufnumDelayR", 2*defSampleRate, 1],
		];
		classData.guiWidth = 550;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumSampleL", 0, \ir],
			["bufnumSampleR", 0, \ir],
			["bufnumSpectrumL", 0, \ir],
			["bufnumSpectrumR", 0, \ir],
			["bufnumDelayL", 0, \ir],
			["bufnumDelayR", 0, \ir],
			["bankNo", 0, 0],
			["sampleNo", 0, 0],
			["inputLevel", 0.5, defLagTime],
			["predelay", 0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInputLevel", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, bufnumSampleL, bufnumSampleR, bufnumSpectrumL, bufnumSpectrumR,
			bufnumDelayL, bufnumDelayR, bankNo, sampleNo, inputLevel,
			predelay, wetDryMix, modInputLevel=0, modWetDryMix=0;
			var input, outSound, inputLevelCombined, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inputLevelCombined = (inputLevel + modInputLevel).max(0).min(1);
			input = TXClean.ar(startEnv * InFeedback.ar(in,2));
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = [
				BufDelayC.ar(bufnumDelayL, PartConv.ar(input* inputLevelCombined, fftsize, bufnumSpectrumL),
					predelay, mixCombined, input * (1-mixCombined)),
				BufDelayC.ar(bufnumDelayR, PartConv.ar(input* inputLevelCombined, fftsize, bufnumSpectrumR),
					predelay, mixCombined, input * (1-mixCombined)),
			];
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["TXPopupActionPlusMinus", "Sample bank", {system.arrSampleBankNames},
				"bankNo",
				{ arg view; this.bankNo = view.value; this.sampleNo = 0;
					this.rebuildSynth; this.loadSample(0);
					this.setSynthArgSpec("sampleNo", 0); system.showView;}
			],
			// array of sample filenames - beginning with blank sample  - mono & stereo files
			["TXListViewActionPlusMinus", "Sample",
				{["No Sample"]++system.sampleFileNames(bankNo, true)},
				"sampleNo", { arg view; this.sampleNo = view.value;
					this.rebuildSynth; this.loadSample(view.value);
					{system.showView;}.defer(0.2);}
			],
			["Spacer", 80],
			["ActionButton", "Add Samples to Sample Bank",
				{TXBankBuilder2.addSampleDialog("Sample", bankNo)}, 200],
			["ActionButton", "Show", {showWaveform = 1; system.showView;},
				80, TXColor.white, TXColor.sysGuiCol2],
			["ActionButton", "Hide", {showWaveform = 0; system.showView;  this.sampleData_(nil);},
				80, TXColor.white, TXColor.sysDeleteCol],
			["NextLine"],
			["TXSoundFileView", {sampleFileName}, {this.sampleData},
				{arg argData; this.sampleData_(argData);}, nil, {showWaveform}],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Input level", ControlSpec(0, 1, \amp), "inputLevel"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Pre-delay", ControlSpec(0.001, 1, \amp), "predelay"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
	}

	extraSaveData { // override default method
		^[sampleNo, sampleFileName, sampleNumChannels, bankNo];
	}

	loadExtraData {arg argData;  // override default method
		sampleNo = argData.at(0);
		sampleFileName = argData.at(1);
		// Convert path
		sampleFileName = TXPath.convert(sampleFileName);
		sampleNumChannels = argData.at(2);
		bankNo = argData.at(3);
		this.buildGuiSpecArray;
		system.showViewIfModDisplay(this);
		{	this.makeBuffersAndSynth(classData.arrBufferSpecs);
			this.loadSample(sampleNo);
		}.defer(0.2);
	}

	loadSample { arg argIndex; // method to load samples into buffer
		var holdBufferL, holdBufferR, holdSpectrumBufSize, holdSpectrumBufL, holdSpectrumBufR,
		holdSampleInd, holdModCondition, holdPath, holdNoChannels = 1, maxSampleTime;

		Routine.run {
			// max is 10 secs
			maxSampleTime = 10;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;
			// adjust index
			holdSampleInd = (argIndex - 1).min(system.sampleFiles(bankNo).size-1);
			if (system.sampleFiles(bankNo).at(holdSampleInd).notNil, {
				holdNoChannels = system.sampleFiles(bankNo).at(holdSampleInd).at(2);
			});
			// check for invalid samples
			if (argIndex == 0 or: {system.sampleFiles(bankNo).at(holdSampleInd).at(3) == false}, {
				// if argIndex is 0, clear the current buffers & filename
				holdBufferL = Buffer.alloc(system.server, 2048, 1,
					bufnum: buffers.at(0).bufnum);
				holdBufferR = Buffer.alloc(system.server, 2048, 1,
					bufnum: buffers.at(1).bufnum);
				sampleFileName = "";
			},{
				// otherwise,  try to load sample.  if it fails, display error message and clear
				holdPath = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
				// Convert path
				holdPath = TXPath.convert(holdPath);
				// check if sample is mono/stereo
				if (holdNoChannels == 1, {
					holdBufferL = Buffer.read(system.server, holdPath,
						numFrames: maxSampleTime * system.server.sampleRate, // max time is 10 secs
						action: { arg argBuffer;
							{
								//	if file loaded ok
								if (argBuffer.notNil, {
									this.setSynthArgSpec("bufnumSample", argBuffer.bufnum);
									sampleFileName = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
									sampleNumChannels = argBuffer.numChannels;
								},{
									buffers.at(0).zero;
									sampleFileName = "";
									TXInfoScreen.new("Invalid Sample File"
										++ system.sampleFiles(bankNo).at(holdSampleInd).at(0));
								});
							}.defer;	// defer because gui process
						},
						// pass buffer number
						bufnum: buffers.at(0).bufnum
					);
					holdBufferR = Buffer.read(system.server, holdPath,
						numFrames: maxSampleTime * system.server.sampleRate,
						action: { arg argBuffer;
							{
								//	if file loaded ok
								if (argBuffer.notNil, {
									this.setSynthArgSpec("bufnumSample", argBuffer.bufnum);
									sampleFileName = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
									sampleNumChannels = argBuffer.numChannels;
								},{
									buffers.at(1).zero;
									sampleFileName = "";
									TXInfoScreen.new("Invalid Sample File"
										++ system.sampleFiles(bankNo).at(holdSampleInd).at(0));
								});
							}.defer;	// defer because gui process
						},
						// pass buffer number
						bufnum: buffers.at(1).bufnum
					);
				},{
					// for stereo
					holdBufferL = Buffer.readChannel(system.server, holdPath, channels: [0],
						numFrames: 20 * system.server.sampleRate,
						action: { arg argBuffer;
							{
								//	if file loaded ok
								if (argBuffer.notNil, {
									this.setSynthArgSpec("bufnumSample", argBuffer.bufnum);
									sampleFileName = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
									sampleNumChannels = argBuffer.numChannels;
								},{
									buffers.at(0).zero;
									sampleFileName = "";
									TXInfoScreen.new("Invalid Sample File"
										++ system.sampleFiles(bankNo).at(holdSampleInd).at(0));
								});
							}.defer;	// defer because gui process
						},
						// pass buffer number
						bufnum: buffers.at(0).bufnum
					);
					holdBufferR = Buffer.readChannel(system.server, holdPath, channels: [1],
						numFrames: 20 * system.server.sampleRate,
						action: { arg argBuffer;
							{
								//	if file loaded ok
								if (argBuffer.notNil, {
									this.setSynthArgSpec("bufnumSample", argBuffer.bufnum);
									sampleFileName = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
									sampleNumChannels = argBuffer.numChannels;
								},{
									buffers.at(1).zero;
									sampleFileName = "";
									TXInfoScreen.new("Invalid Sample File"
										++ system.sampleFiles(bankNo).at(holdSampleInd).at(0));
								});
							}.defer;	// defer because gui process
						},
						// pass buffer number
						bufnum: buffers.at(1).bufnum
					);
				});
			});

			// pause
			system.server.sync;
			holdSpectrumBufSize= PartConv.calcBufSize(fftsize, holdBufferL);
			holdSpectrumBufL= Buffer.alloc(system.server, holdSpectrumBufSize, 1,
				bufnum: buffers.at(2).bufnum);
			holdSpectrumBufR= Buffer.alloc(system.server, holdSpectrumBufSize, 1,
				bufnum: buffers.at(3).bufnum);
			holdSpectrumBufL.preparePartConv(holdBufferL, fftsize);
			holdSpectrumBufR.preparePartConv(holdBufferR, fftsize);
			// pause
			system.server.sync;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		}; // end of Routine.run
	} // end of method loadSample


}

