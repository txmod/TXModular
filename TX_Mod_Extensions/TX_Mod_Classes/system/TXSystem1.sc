// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSystem1 {		// system module 1

	/*	use the following code to show all class methods:

		TXSystem1.class.dumpMethodList;
	*/
	////////////////////////////////////////////////////////////////////////////////////
	//	define class variables:

	classvar	<systemVersion = "086";	// version of the TX Modular system shown in gui
	classvar	<systemRevision = 1001;	// current revision no of the system

	classvar	<arrAllPossModules;		// array of all possible modules.
	classvar	<arrAllPossCurSeqModules;	// array of all possible current sequencer modules.
	classvar	<arrAllPossOldSeqModules;	// array of all possible older sequencer modules (for old builds)
	classvar	<arrSystemModules;			// array of modules in system.
	classvar	<arrModuleClipboards;		// array of modules clipboards for copying/pasting.

	classvar	<arrMainOutBusses;		// array of main out busses
	classvar	<arrFXSendBusses;		// array of FX send busses
	classvar	<arrAudioAuxBusses;	// array of Audio Aux busses
	classvar	<arrControlAuxBusses;	// array of Control Aux busses
	classvar	<arrAllBusses;		// single array of all busses used

	classvar <server;				// server variable
	classvar <moduleID = 99;		// system itself has a moduleID of 99;
	classvar w;
	classvar <groupModules, <groupChannels;  // groups for order of execution
	classvar <>autoOpen = false;
	classvar <>autoRun = true;
	classvar <>latency = 0.1;		// general latency time in seconds to allow server to do something
	classvar <arrScreenUpdFuncs;	// for gui
	classvar <instName = "System";	// system name
	classvar <arrActionSpecs;		// specs for system actions available to widgets
	classvar <guiSpecTitleArray;
	classvar <>showFrontScreen;
	classvar <showSystemControls;
	classvar <arrSnapshots;			// array of system snapshots
	classvar <>snapshotNo = 0;
	classvar <>snapshotName = "";
	classvar <holdLoadQueue;
	classvar <defSampleRate = 44100;	//	default sample rate. (also set in TXModuleBase)
	classvar <txStandAlone = 0;
	classvar <>arrModulesForMeters;	// holds all modules that could be displayed on meters

	// bypass globalMouseDown test for now, can cause crashes
	//classvar <globalMouseDown;		// mouse button variables:
	//classvar mouseButtonResponder;
	//classvar mouseButtonSynth;

	classvar viewBox;				// for gui
	classvar <showWindow;			// for gui

	classvar mainWindowWidth = 1390;	// width and height of main window
	classvar mainWindowHeight = 786;
	classvar closingDown = false;	// only used when closing down
	classvar screenChanged = false;	// for gui
	classvar screenRebuild = false;	// for gui
	classvar holdFileName = "";		// for gui

	classvar holdServerOptions;		// to hold server options
	classvar holdNextModuleID;		// used to give each module a unique ID
	classvar headerBox;			// for gui
	classvar stTextFileName;		// for gui
	classvar screenUpdRoutine;		// for gui
	classvar holdBootSeconds;		// for gui
	classvar historyEvents;			// for gui
	classvar historyIndex;			// for gui
	classvar notes0, notes1, notes2, notes3, notes4, notes5, notes6, notes7;   // for notes view gui

	classvar arrMeters;			// holds any meters
	classvar <dataBank;				// event to hold data
	classvar <>audioSetupQuit = false;	// for final closing down
	classvar <arrPresets;			// dummy array ofpresets

	////////////////////////////////////////////////////////////////////////////////////
	// Define class methods

	*initClass{
		holdBootSeconds = Main.elapsedTime;
		// create event and set variable:
		dataBank = ();
		dataBank.modulesVisibleOrigin = Point.new(0,0);
	}
	// start the system

	*startCocoa {
		this.start;
	}


	*start { arg argStandAlone = 0, argFileNameString, showAudioOptions = true;
		var holdString;
		if (argStandAlone == 1, {
			txStandAlone = 1; // for running TX system as a standalone
			// removed for now
			//		showAudioOptions = false; // force to false if standalone
		});

		// removed for now
		//	GUI.cocoa; // use cocoa in subsequent GUI creation procedures

		//GUI.skin.fontSpecs = [ "Helvetica", 9 ];
		Font.default =  Font("Helvetica", 12);

		//  set variables:
		dataBank.loadingDataFlag = false;
		dataBank.arrModulesForRebuilding = [].asSet;
		dataBank.confirmDeletions = true;
		dataBank.windowAlpha = 1;
		dataBank.windowColour = TXColour.sysMainWindow;
		dataBank.imageFileName = nil;
		dataBank.displayModeIndex = 1;
		dataBank.holdImage = nil;
		dataBank.volume = 0;
		dataBank.ipAddress = "__________ ";
		this.updateIPAddress;
		dataBank.showAudioOptions = showAudioOptions;
		this.loadSystemSettings;
		this.clearHistory;

		// check argFileNameString
		if (argFileNameString.isNil, {
			holdString = TXSystem1.filenameSymbol.asString.dirname
			++ "/TXDefaultSystemData/TXDefaultSystem";
			if( File.exists(holdString), {
				argFileNameString = holdString;
			});
		});
		if (argFileNameString.isNil, {showSystemControls = 1}, {showSystemControls = 0});

		// set start window
		if (argFileNameString.notNil, {
			showWindow = "Run Interface";
			showFrontScreen = true;
		},{
			showWindow = "Modules & Channels";
			showFrontScreen = false;
		});

		// bypass globalMouseDown test for now - causes crashes
		//	globalMouseDown = false;

		// create Load Queue
		holdLoadQueue = TXLoadQueue.new;
		// create array of all possible modules
		arrAllPossModules = [

			// required system modules
			TXChannel,
			TXBusMainOuts,
			TXBusFXSend,
			TXBusAudioAux,
			TXBusControlAux,
			//
			//	N.B. ADD ANY NEW MODULES INTO THIS LIST (by alpha) AND FOLLOWING ONES (by category):  --->
			//
			// other modules in ALPHA order, by their defaultName
			TXActionSeq3,
			TXActionSlider,
			TXAmpFollower3,
			TXAmpSim,
			TXAmpSimSt,
			TXAnalyser3,
			TXAnimateCode2,
			TXAudioIn4,
			TXAudioTrigger3,
			TXBitCrusher2,
			TXBitCrusher2St,
			TXChorus,
			TXChorusSt,
			TXCodeInsertAu,
			TXCodeInsertAuSt,
			TXCodeInsertCtrl,
			TXCodeSourceAu,
			TXCodeSourceAuSt,
			TXCodeSourceCtrl,
			TXCompander3,
			TXCompander3St,
			TXControlDelay4,
			TXConvolution,
			TXConvolutionSt,
			TXCyclOSCCol,
			TXCyclOSCGrey,
			TXDCRemove,
			TXDCRemoveSt,
			TXDelay4,
			TXDelay4St,
			TXDisintegrator,
			TXDisintegratorSt,
			TXDistortion2,
			TXDistortion2St,
			TXEnv16Stage,
			TXEnvCurve,
			TXEnvDADSR4,
			TXEnvFollow,
			TXEnvFollowSt,
			TXEQGraphic,
			TXEQGraphicSt,
			TXEQPara,
			TXEQParaSt,
			TXEQShelf,
			TXEQShelfSt,
			TXFilePlayer6,
			TXFilePlayer6St,
			TXFileRecorder2,
			TXFileRecorder2St,
			TXFilterExt2,
			TXFilterExt2St,
			TXFilterSynth2,
			TXFlanger3,
			TXFlanger3St,
			TXFMSynth4,
			TXGain,
			TXGainSt,
			TXGranulator,
			TXGroupMorph,
			TXHarmoniser,
			TXLFOMulti2,
			TXLFOCurve,
			TXLimiter,
			TXLimiterSt,
			TXLiveLooper2,
			TXLoopPlayer,
			TXLoopPlayerSt2,
			TXMatrixAudio8x8,
			TXMatrixControl8x8,
			TXMIDIController2,
			TXMIDIControlOut3,
			TXMIDINote2,
			TXMIDIOut,
			TXMIDIPitchbend2,
			TXMIDIVelocity2,
			TXMixAudio8to1,
			TXMixAudio16to2,
			TXMixControl8to1,
			TXMonoToStereo,
			TXMidSideDecoder,
			TXMidSideEncoder,
			TXMultiTapDelay2,
			TXNoiseWhitePink,
			TXNormalizer,
			TXNormalizerSt,
			TXNotchPhaser,
			TXNotchPhaserSt,
			TXNoteStacker,
			TXOSCController,
			TXOSCController2D,
			TXOSCControlOut,
			TXOSCOut,
			TXOSCRemote,
			TXOSCTrigger,
			TXPerlinNoise,
			TXPhaser,
			TXPItchFollower4,
			TXPitchShifter,
			TXPitchShifterSt,
			TXPingPong,
			TXPingPongSt,
			TXPluckSynth,
			TXQCParticles2,
			TXQCPlayer4,
			TXReverb2,
			TXReverbSt2,
			TXReverbA,
			TXReverbF,
			TXReverbFSt,
			TXReverbG,
			TXRingMod2,
			TXSampleHold,
			TXSamplePlayer5a,
			TXSamplePlayerSt6,
			TXSamplePlayerPlus,
			TXSamplePlayerPlusSt,
			TXSimpleSlider2,
			TXSlope,
			TXSmooth2,
			TXSpectralFX,
			TXStepSequencer,
			TXStereoToMono2,
			TXStereoWidth,
			TXTableSynth4,
			TXTransientShape,
			TXTransientShapeSt,
			TXTrigImpulse,
			TXVocoder2,
			TXVocoderFX2,
			TXVosim,
			TXWarp2,
			TXWaveform5,
			TXWaveform5St,
			TXWaveshaper,
			TXWaveshaperSt,
			TXWaveSynth8,
			TXWaveSynthPlus2,
			TXWaveTerrain,		TXWiiController,
			TXWiiControllerOSC3,
			TXWiiTrigger,
			TXWiiTrigOSC2,
			TXXDistort,
			TXXDistortSt,
			TXWXFader2to1,
			TXWXFader2to1C,
			TXWXFader4to2,
		];
		// the following strings are formatted as popup item with categories and modules
		dataBank.arrSourceModulesByCategory = [
		["Audio: Drone", "Noise White-Pink", TXNoiseWhitePink],
		["Audio: Drone", "Vosim", TXVosim],
		["Audio: Drone", "Waveform ", TXWaveform5],
		["Audio: Drone", "Waveform St", TXWaveform5St],
		["Audio: Drone", "Wave Terrain", TXWaveTerrain],
		["Audio: File", "File Player ", TXFilePlayer6],
		["Audio: File", "File Player St", TXFilePlayer6St],
		["Audio: Inputs", "Audio Inputs", TXAudioIn4],
		["Audio: Mid-Side Encoding", "M-S Decoder", TXMidSideDecoder],
		["Audio: Mid-Side Encoding", "M-S Encoder", TXMidSideEncoder],
		["Audio: Mixing", "Matrix A 8x8 ", TXMatrixAudio8x8],
		["Audio: Mixing", "Mix Audio 8-1 ", TXMixAudio8to1],
		["Audio: Mixing", "Mix Audio 16-2", TXMixAudio16to2],
		["Audio: Mixing", "X-Fader", TXWXFader2to1],
		["Audio: Mixing", "X-Fader St", TXWXFader4to2],
		["Audio: Polyphonic", "Filter Synth", TXFilterSynth2],
		["Audio: Polyphonic", "FM Synth", TXFMSynth4],
		["Audio: Polyphonic", "Granulator", TXGranulator],
		["Audio: Polyphonic", "Loop Player", TXLoopPlayer],
		["Audio: Polyphonic", "Loop Player St", TXLoopPlayerSt2],
		["Audio: Polyphonic", "Pluck", TXPluckSynth],
		["Audio: Polyphonic", "Sample Player", TXSamplePlayer5a],
		["Audio: Polyphonic", "Sample Player St", TXSamplePlayerSt6],
		["Audio: Polyphonic", "Sample Player+ ", TXSamplePlayerPlus],
		["Audio: Polyphonic", "Sample Player+ St", TXSamplePlayerPlusSt],
		["Audio: Polyphonic", "Table Synth", TXTableSynth4],
		["Audio: Polyphonic", "Wave Synth", TXWaveSynth8],
		["Audio: Polyphonic", "Wave Synth+", TXWaveSynthPlus2],
		["Audio: SC Code", "Code Source A", TXCodeSourceAu],
		["Audio: SC Code", "Code Source A St", TXCodeSourceAuSt],
		["Control: Analysis", "Amp Follower", TXAmpFollower3],
		["Control: Analysis", "Analyser", TXAnalyser3],
		["Control: Analysis", "Audio Trigger", TXAudioTrigger3],
		["Control: Analysis", "CyclOSC Colour", TXCyclOSCCol],
		["Control: Analysis", "CyclOSC Grey", TXCyclOSCGrey],
		["Control: Analysis", "Pitch Follower", TXPItchFollower4],
		["Control: Envelopes", "Env 16-stage", TXEnv16Stage],
		["Control: Envelopes", "Env Curve", TXEnvCurve],
		["Control: Envelopes", "Env DADSR", TXEnvDADSR4],
		["Control: Envelopes", "Trigger Impulse", TXTrigImpulse],
		["Control: MIDI", "MIDI Controller", TXMIDIController2],
		["Control: MIDI", "MIDI Control Out", TXMIDIControlOut3],
		["Control: MIDI", "MIDI Note", TXMIDINote2],
		["Control: MIDI", "MIDI Out ", TXMIDIOut],
		["Control: MIDI", "MIDI Pitchbend", TXMIDIPitchbend2],
		["Control: MIDI", "MIDI Velocity", TXMIDIVelocity2],
		["Control: Mixing", "Group Morph", TXGroupMorph],
		["Control: Mixing", "Matrix C 8x8", TXMatrixControl8x8],
		["Control: Mixing", "Mix Control 8-1", TXMixControl8to1],
		["Control: Mixing", "X-Fader 2-1 C", TXWXFader2to1C],
		["Control: Modulation", "LFO", TXLFOMulti2],
		["Control: Modulation", "LFO Curve", TXLFOCurve],
		["Control: Modulation", "Perlin Noise", TXPerlinNoise],
		["Control: OSC", "OSC Controller", TXOSCController],
		["Control: OSC", "OSC Control 2D ", TXOSCController2D],
		["Control: OSC", "OSC Control Out", TXOSCControlOut],
		["Control: OSC", "OSC Out", TXOSCOut],
		["Control: OSC", "OSC Remote", TXOSCRemote],
		["Control: OSC", "OSC Trigger", TXOSCTrigger],
		["Control: Note Stacker", "Note Stacker", TXNoteStacker],
		["Control: Sequencer", "Action Sequencer", TXActionSeq3],
		["Control: Sequencer", "Step Sequencer", TXStepSequencer],
		["Control: Slider", "Action Slider", TXActionSlider],
		["Control: Slider", "Simple Slider", TXSimpleSlider2],
		["Control: SC Code", "Code Source C", TXCodeSourceCtrl],
		["Control: Visual", "QC Particles", TXQCParticles2],
		["Control: Visual", "Quartz Player", TXQCPlayer4],
		["Control: Visual", "Animate Code", TXAnimateCode2],
		["Control: Wii", "Wii Ctrl Darwiin", TXWiiController],
		["Control: Wii", "Wii Ctrl OSC", TXWiiControllerOSC2],
		["Control: Wii", "Wii Trig Darwiin", TXWiiTrigger],
		["Control: Wii", "Wii Trig OSC", TXWiiTrigOSC2],
	];

		dataBank.arrAudioInsertModulesByCategory = [
			[">AUDIO INSERTS: ", nil 			],
			[">Channel tools ", nil 			],
			["   Mono to Stereo", TXMonoToStereo],
			["   Stereo To Mono", TXStereoToMono2],
			["   Stereo Width", TXStereoWidth],
			[">Delay ", nil 			],
			["   Delay", TXDelay4],
			["   Delay St", TXDelay4St],
			["   Live Looper", TXLiveLooper2],
			["   MultiTap Delay", TXMultiTapDelay2],
			["   Ping Pong", TXPingPong],
			["   Ping Pong St", TXPingPongSt],
			[">Distortion ", nil 			],
			["   Amp Sim", TXAmpSim],
			["   Amp Sim St", TXAmpSimSt],
			["   Bit Crusher", TXBitCrusher2],
			["   Bit Crusher St", TXBitCrusher2St],
			["   Disintegrator", TXDisintegrator],
			["   Disintegrator St", TXDisintegratorSt],
			["   Distortion", TXDistortion2],
			["   Distortion St", TXDistortion2St],
			["   Waveshaper", TXWaveshaper],
			["   Waveshaper St", TXWaveshaperSt],
			["   X Distort", TXXDistort],
			["   X Distort St", TXXDistortSt],
			[">Dynamics ", nil 			],
			["   Compander", TXCompander3],
			["   Compander St", TXCompander3St],
			["   Env Follow", TXEnvFollow],
			["   Env Follow St", TXEnvFollowSt],
			["   Gain", TXGain],
			["   Gain St", TXGainSt],
			["   Limiter", TXLimiter],
			["   Limiter St", TXLimiterSt],
			["   Normalizer", TXNormalizer],
			["   Normalizer St", TXNormalizerSt],
			["   Transient Shape", TXTransientShape],
			["   Transient Shape St", TXTransientShapeSt],
			[">EQ & Filter ", nil ],
			["   DC Remove", TXDCRemove],
			["   DC Remove St", TXDCRemoveSt],
			["   EQ Graphic", TXEQGraphic],
			["   EQ Graphic St", TXEQGraphicSt],
			["   EQ Para", TXEQPara],
			["   EQ Para St", TXEQParaSt],
			["   EQ Shelf", TXEQShelf],
			["   EQ Shelf St", TXEQShelfSt],
			["   Filter", TXFilterExt2],
			["   Filter St", TXFilterExt2St],
			[">Modulation ", nil ],
			["   Chorus", TXChorus],
			["   Chorus St", TXChorusSt],
			["   Flanger", TXFlanger3],
			["   Flanger St", TXFlanger3St],
			["   Notch Phaser", TXNotchPhaser],
			["   Notch Phaser St", TXNotchPhaserSt],
			["   Phaser", TXPhaser],
			["   Ring Modulator", TXRingMod2],
			[">Pitch Shift ", nil ],
			["   Pitch Shifter", TXPitchShifter],
			["   Pitch Shifter St", TXPitchShifterSt],
			[">Recording ", nil ],
			["   File Recorder", TXFileRecorder2],
			["   File Recorder St", TXFileRecorder2St],
			[">Reverb ", nil ],
			["   Convolution ", TXConvolution],
			["   Convolution St ", TXConvolutionSt],
			["   Reverb", TXReverb2],
			["   Reverb St", TXReverbSt2],
			["   ReverbA", TXReverbA],
			["   ReverbF", TXReverbF],
			["   ReverbF St", TXReverbFSt],
			["   ReverbG", TXReverbG],
			[">Spectral ", nil ],
			["   Spectral FX", TXSpectralFX],
			["   Vocoder", TXVocoder2],
			["   Vocoder FX", TXVocoderFX2],
			[">SuperCollider Code ", nil ],
			["   Code Insert A", TXCodeInsertAu],
			["   Code Insert A St", TXCodeInsertAuSt],
			[">Synthesis ", nil ],
			["   Harmoniser", TXHarmoniser],
		];

		dataBank.arrControlInsertModulesByCategory = [
			[">CONTROL INSERTS: ", nil ],
			[">Analysis ", nil ],
			["   Slope", TXSlope],
			[">Delay ", nil ],
			["   Control Delay", TXControlDelay4],
			[">Modify ", nil ],
			["   Smooth", TXSmooth2],
			["   Warp ", TXWarp2],
			["   Sample and Hold", TXSampleHold],
			[">SuperCollider Code ", nil ],
			["    Code Insert C", TXCodeInsertCtrl],
		];
		// adjust for cocoa
		if (GUI.current.asSymbol != \cocoa, {
			arrAllPossModules.remove(TXQCParticles2);
			arrAllPossModules.remove(TXQCPlayer4);
			dataBank.arrSourceModulesByCategory.remove(["Control: Visual", "QC Particles", TXQCParticles2]);
			dataBank.arrSourceModulesByCategory.remove(["Control: Visual", "Quartz Player", TXQCPlayer4]);
		});
		// create arrays of all possible old and current sequencer modules
		arrAllPossOldSeqModules = [	// kept for instruments saved using earlier builds
			TXSequencer4,
			TXSequencer5,
			TXSequencer6,
			TXNoteSequencer,
			TXNoteSequencer2,
			TXActionSeq,
			TXActionSeq2,
		];
		arrAllPossCurSeqModules = [
			TXActionSeq3,
			TXStepSequencer,
		];
		autoOpen = false;
		closingDown = false;

		// initialise
		notes0 = " ";
		notes1 = " ";
		notes2 = " ";
		notes3 = " ";
		notes4 = " ";
		notes5 = " ";
		notes6 = " ";
		notes7 = " ";
		arrSnapshots  = Array.newClear(100);
		snapshotNo = 0;
		snapshotName = "";
		arrModuleClipboards = Dictionary.new;

		// assign  server
		//	if (txStandAlone == 1, {
		//		server = Server.internal;
		//	},{//		server = Server.local;
		//	});	// server now always internal
		// Platform.case(
		// 	\osx,   { server = Server.internal },
		// 	\linux, { server = Server.default }
		// );
		//Server.default = server;

		server = Server.default;
		server.quit;

		audioSetupQuit = false;
		this.startMain(argFileNameString);

	}	// end of *start method

	*startMain{ arg argFileNameString;
		var	holdFileName, holdData, holdFileNameString, holdString;	var holdInfoScreen, holdStartFunction;

		// Create a new instance of ServerOptions
		holdServerOptions = ServerOptions.new;
		holdServerOptions.numOutputBusChannels = 16;
		holdServerOptions.numAudioBusChannels = 128 * 4;
		holdServerOptions.numControlBusChannels = 4096 * 4;
		holdServerOptions.memSize = 8192* 16;
		holdServerOptions.numWireBufs = 64* 8;
		holdServerOptions.zeroConf = true;
		holdServerOptions.verbosity = -1;
		holdServerOptions.device = dataBank.audioDevice;
		holdServerOptions.hardwareBufferSize = dataBank.bufferSize;
		holdServerOptions.sampleRate = dataBank.sampleRate ? defSampleRate;

		holdStartFunction = {

			// Show starting info screen
			server.options = holdServerOptions;
			holdInfoScreen = TXInfoScreen.new("   TX MODULAR SYSTEM - STARTING . . .",
				0, TXColor.sysGuiCol1, 20, 800 );

			server.waitForBoot({		// wait for server to boot before running other actions
				// use Routine to pause - for safety
				Routine.run {
					// set server.latency
					server.latency = latency;
					// pause longer if standalone
					if (txStandAlone == 1, {
						0.5.wait;
					},{
						0.1.wait;
					});

					{ // defer function

						// close info screen
						{holdInfoScreen.close;}.defer;
						// set class variables
						guiSpecTitleArray = [
							["xxxxx"]    // dummy  used to override behaviour in TXBuildActions
						];
						arrActionSpecs = TXBuildActions.from( this, [
							["commandAction", "Refresh screen", {this.flagGuiUpd;}],
							["commandAction", "Sync Start", {this.syncStart;}],
							["commandAction", "Sync Stop", {this.syncStop;}],
							["commandAction", "Stop all syncable modules",
								{this.stopAllSyncModules; this.showView;}],
							["commandAction", "Panic - stop all notes", {this.allNotesOff;}],
							["EmptyValueAction", \popup],
							["TXPopupAction", "Snapshot",
								{["Load Snapshot ..."] ++
									(1 .. 99).collect({arg item, i;
										var holdName;
										holdName = this.getSnapshotName(item);
										if (holdName == "", {holdName = "EMPTY"});
										"Snapshot " ++ item.asString ++ ": " ++ holdName;
									});},
								"Snapshot",
								{arg view;
									this.loadSnapshot(view.value);
								}
							],
							["commandAction", "Show Screen 1", {
								TXFrontScreen.storeCurrLoadNewLayer(0); this.showView;}],
							["commandAction", "Show Screen 2", {
								TXFrontScreen.storeCurrLoadNewLayer(1); this.showView;}],
							["commandAction", "Show Screen 3", {
								TXFrontScreen.storeCurrLoadNewLayer(2); this.showView;}],
							["commandAction", "Show Screen 4", {
								TXFrontScreen.storeCurrLoadNewLayer(3); this.showView;}],
							["commandAction", "Show Screen 5", {
								TXFrontScreen.storeCurrLoadNewLayer(4); this.showView;}],
							["commandAction", "Show Screen 6", {
								TXFrontScreen.storeCurrLoadNewLayer(5); this.showView;}],
							["commandAction", "Show Screen 7", {
								TXFrontScreen.storeCurrLoadNewLayer(6); this.showView;}],
							["commandAction", "Show Screen 8", {
								TXFrontScreen.storeCurrLoadNewLayer(7); this.showView;}],
							["commandAction", "Show Screen 9", {
								TXFrontScreen.storeCurrLoadNewLayer(8); this.showView;}],
							["commandAction", "Show Screen 10", {
								TXFrontScreen.storeCurrLoadNewLayer(9); this.showView;}],
							["commandAction", "Show Screen 11", {
								TXFrontScreen.storeCurrLoadNewLayer(10); this.showView;}],
							["commandAction", "Show Screen 12", {
								TXFrontScreen.storeCurrLoadNewLayer(11); this.showView;}],
							["commandAction", "Show Screen 13", {
								TXFrontScreen.storeCurrLoadNewLayer(12); this.showView;}],
							["commandAction", "Show Screen 14", {
								TXFrontScreen.storeCurrLoadNewLayer(13); this.showView;}],
							["commandAction", "Show Screen 15", {
								TXFrontScreen.storeCurrLoadNewLayer(14); this.showView;}],
							["commandAction", "Show Screen 16", {
								TXFrontScreen.storeCurrLoadNewLayer(15); this.showView;}],
							["commandAction", "Show Screen 17", {
								TXFrontScreen.storeCurrLoadNewLayer(16); this.showView;}],
							["commandAction", "Show Screen 18", {
								TXFrontScreen.storeCurrLoadNewLayer(17); this.showView;}],
							["commandAction", "Show Screen 19", {
								TXFrontScreen.storeCurrLoadNewLayer(18); this.showView;}],
							["commandAction", "Show Screen 20", {
								TXFrontScreen.storeCurrLoadNewLayer(19); this.showView;}],
							["commandAction", "Show Next Screen", {
								TXFrontScreen.storeCurrLoadNextLayer; this.showView;}],
							["commandAction", "Show Previous Screen", {
								TXFrontScreen.storeCurrLoadPrevLayer; this.showView;}],
							["EmptyValueAction", \number],
							["EmptyValueAction", \checkbox],
							["EmptyValueAction", \text],
							["EmptyValueAction", \textedit],
							["EmptyValueAction", \ipaddress],
						]);
						// start MIDI
						try {this.midiConnect};

						// initialise
						holdNextModuleID = 100;
						arrSystemModules = [];
						// create groups
						groupModules = Group.tail(server);
						groupChannels = Group.tail(server);

						// initialise all module classes
						arrAllPossModules.do({ arg item, i;
							item.initClass;
							item.system = this;
							if (item.moduleType == "channel",
								{item.group = groupChannels;},
								{item.group = groupModules;}
							);
						});
						// initialise other classes
						//			TXModGui.initClass;
						//			TXModGui.system = this;
						//			TXSeqGui.initClass;
						//			TXSeqGui.system = this;
						TXBankBuilder2.system = this;
						TXBankBuilder2.initClass;
						TXChannelRouting.system = this;
						TXChannelRouting.initClass;
						TXSignalFlow.system = this;
						TXSignalFlow.initClass;
						TXChannel.systemInit;
						TXGuiBuild2.system = this;
						TXWidget.system = this;
						TXWidget.initClass;
						TXFrontScreen.system = this;
						TXFrontScreen.initClass;
						// initialise mouse synth for checking mouse state
						// 	NOTE - MouseSynth removed for now, can cause crashes
						//	this.startMouseSynth;
						// create stereo Main Outs Busses
						arrMainOutBusses =
						["Main Outs"]
						.collect({ arg item, i;
							TXBusMainOuts.new(item);
						});
						// create mono FX Send Busses
						arrFXSendBusses =
						["FX Send 1", "FX Send 2", "FX Send 3", "FX Send 4"]
						.collect({ arg item, i;
							TXBusFXSend.new(item);
						});
						// create Audio Aux Busses
						arrAudioAuxBusses =
						["Audio Aux 1+2", "Audio Aux 3+4", "Audio Aux 5+6",
							"Audio Aux 7+8", "Audio Aux 9+10"]
						.collect({ arg item, i;
							TXBusAudioAux.new(item);
						});
						// create Audio Aux Busses
						arrControlAuxBusses =
						["Control Aux 1", "Control Aux 2", "Control Aux 3",
							"Control Aux 4", "Control Aux 5",
							"Control Aux 6", "Control Aux 7", "Control Aux 8",
							"Control Aux 9", "Control Aux 10"]
						.collect({ arg item, i;
							TXBusControlAux.new(item);
						});
						// create arrAllBusses
						arrAllBusses = arrMainOutBusses ++ arrFXSendBusses ++ arrAudioAuxBusses
						++ arrControlAuxBusses;
						// Initialise module layout x/y positions of all busses
						this.initBusPositions;
						// create gui
						this.makeGui;
					}.defer;

					// pause
					0.1.wait;

					// create routine to keep screen up to date
					screenUpdRoutine = Routine { arg inval;
						loop {
							// run all the screen update functions
							this.runScreenUpdFuncs;
							0.03.wait;	// update every xxx secs
						};
					}.play;
					// if file name passed then open it
					if (argFileNameString.notNil, {
						holdFileNameString = argFileNameString.resolveRelative;
						holdData = thisProcess.interpreter.executeFile(holdFileNameString);
						holdFileName = "    File name: " ++ holdFileNameString;
						this.loadData(holdData);
					});
				};	// end of Routine.run

			});	// end of server.waitForBoot

		};  // end of holdStartFunction

		if (dataBank.showAudioOptions == false, {
			holdStartFunction.value;
		},{
			if (audioSetupQuit == false, {
				TXAudioOptionsScreen.makeWindow (this, holdServerOptions, holdStartFunction);
			});
		});
	}

	///  SYSTEM SETTINGS /////////////////////////////////////

	*saveSystemSettings {
		var holdFile, holdFileData, holdPath, holdPath2;

		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular/TXModSettings.tx");

		holdFileData = ["TXModSystemSystemSettingsData",
			[dataBank.audioDevice, dataBank.bufferSize, dataBank.sampleRate,
				dataBank.confirmDeletions, dataBank.windowAlpha, dataBank.windowColour.red,
				dataBank.windowColour.green, dataBank.windowColour.blue,
				dataBank.windowColour.alpha, dataBank.imageFileName, dataBank.displayModeIndex,
				dataBank.audioInDevice, dataBank.audioOutDevice
			]
		];
		holdFile = File(holdPath.fullPath, "w");

		if (File.exists(holdPath.fullPath)) {
			/*  OLD CODE
			holdFile << "#" <<< holdFileData << "\n";
			//	use file as an io stream
			//	<<< means store the compile string of the object
			//	<< means store a print string of the object
			NEW CODE:*/
			holdFile.write("#" ++  holdFileData.asCompileString ++ "\n");
		} {
			"".postln;
			"TX Warning: TXSystem1.saveSystemSettings: File doesn't exist: ".postln;
			holdPath.postln;
		};
		holdFile.close;
	}

	*loadSystemSettings {
		var validData, holdPath, holdFile, holdFileData;

		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular");
		//OLD holdFile = PathName.new(holdPath.pathOnly ++ "TxModSettings.tx");
		//NEW:
		holdFile = PathName.new(holdPath.pathOnly ++ "TXModSettings.tx");

		// if TXModular directory doesn't exist, create it.
		if (holdPath.isFolder.not, {
			// OLD CODE
			// FIXME: this won't work on Windows
			// ("mkdir" + holdPath.fullPath).unixCmd;
			// NEW CODE
			holdPath.asString.makeDir;
		});

		if (File.exists(holdFile.fullPath),  {
			// if file TXMODSettings.tx  exists, update values.
			holdFileData = thisProcess.interpreter.executeFile(holdFile.fullPath);
			if (holdFileData.class == Array, {
				if (dataBank.showAudioOptions == true, {
					dataBank.audioDevice = holdFileData[1][0];
					dataBank.bufferSize = holdFileData[1][1];
					dataBank.sampleRate = holdFileData[1][2];
					dataBank.audioInDevice = holdFileData[1][11];
					dataBank.audioOutDevice = holdFileData[1][12];
				});
				if (holdFileData[1][3].notNil, {
					dataBank.confirmDeletions = holdFileData[1][3];
				});
				if (holdFileData[1][4].notNil, {
					dataBank.windowAlpha = holdFileData[1][4];
					this.showView;
				});
				if (holdFileData[1][5].notNil, {
					dataBank.windowColour = Color(holdFileData[1][5], holdFileData[1][6],
						holdFileData[1][7], holdFileData[1][8]);
				});
				dataBank.imageFileName =  holdFileData[1][9];
				dataBank.holdImage = nil;
				if (holdFileData[1][10].notNil, {
					dataBank.displayModeIndex = holdFileData[1][10];
				});
				validData = true;
			});
		});
		if (validData != true,  {
			// if file TXMODSettings.tx  doesn't exist, create it.
			this.saveSystemSettings;
		});
	}

	///  SYNC START AND STOP SYSTEM /////////////////////////////////////

	*syncStart{
		this.syncStartSequencers;
		this.syncStartRecorders;
		this.syncStartPlayers;
	}

	*syncStop{
		this.syncStopSequencers;
		this.syncStopRecorders;
		this.syncStopPlayers;
	}

	*stopAllSyncModules {
		this.stopAllSequencers;
		this.stopAllRecorders;
		this.stopAllPlayers;
	}
	/////// called by above 3: //////

	*syncStartSequencers{
		(this.arrAllPossCurSeqModules ++ this.arrAllPossOldSeqModules).do ({ arg item, i;
			item.syncStartAllSequencers;
		});
	}
	*syncStopSequencers{
		(this.arrAllPossCurSeqModules ++ this.arrAllPossOldSeqModules).do ({ arg item, i;
			try{item.syncStopAllSequencers;};
		});
	}
	*stopAllSequencers{
		(this.arrAllPossCurSeqModules ++ this.arrAllPossOldSeqModules).do ({ arg item, i;
			item.stopAllSequencers;
		});
	}
	*syncStartRecorders{
		TXFileRecorder.syncStartAllRecorders;
		TXFileRecorderSt.syncStartAllRecorders;
		TXFileRecorder2.syncStartAllRecorders;
		TXFileRecorder2St.syncStartAllRecorders;
	}
	*syncStopRecorders{
		TXFileRecorder2.syncStopAllRecorders;
		TXFileRecorder2St.syncStopAllRecorders;
	}
	*stopAllRecorders {
		TXFileRecorder2.stopAllRecorders;
		TXFileRecorder2St.stopAllRecorders;
	}
	*syncStartSeqsAndRecorders{
		this.syncStartSequencers;
		this.syncStartRecorders;
	}
	*syncStartPlayers{
		TXFilePlayer3.syncStartAllPlayers;
		TXFilePlayer3St.syncStartAllPlayers;
		TXFilePlayer4.syncStartAllPlayers;
		TXFilePlayer4St.syncStartAllPlayers;
		TXFilePlayer5.syncStartAllPlayers;
		TXFilePlayer5St.syncStartAllPlayers;
		TXFilePlayer6.syncStartAllPlayers;
		TXFilePlayer6St.syncStartAllPlayers;
	}
	*syncStopPlayers{
		TXFilePlayer3.syncStopAllPlayers;
		TXFilePlayer3St.syncStopAllPlayers;
		TXFilePlayer4.syncStopAllPlayers;
		TXFilePlayer4St.syncStopAllPlayers;
		TXFilePlayer5.syncStopAllPlayers;
		TXFilePlayer5St.syncStopAllPlayers;
		TXFilePlayer6.syncStopAllPlayers;
		TXFilePlayer6St.syncStopAllPlayers;
	}
	*stopAllPlayers {
		TXFilePlayer3.stopAllPlayers;
		TXFilePlayer3St.stopAllPlayers;
		TXFilePlayer4.stopAllPlayers;
		TXFilePlayer4St.stopAllPlayers;
		TXFilePlayer5.stopAllPlayers;
		TXFilePlayer5St.stopAllPlayers;
		TXFilePlayer6.stopAllPlayers;
		TXFilePlayer6St.stopAllPlayers;
	}
	////////////////////////////////////////////////////////////////////////////////////
	*instSortingName {
		^instName;
	}
	/*	NOTE - removed for now, can cause crashes
		*startMouseSynth{
		var mouseTrigID;
		// create unique id
		mouseTrigID = UniqueID.next;
		// defer
		{
		SynthDef("mouseButtonTrig",{|rate= 10|
		var trig, mouseVal;
		mouseVal = MouseButton.kr(0, 1, 0);
		// trigger mouse value to be sent when value changes
		trig = Trig.kr(HPZ1.kr(mouseVal).abs, 0.1);
		SendTrig.kr( trig, mouseTrigID, mouseVal);
		}).send(server);
		mouseButtonResponder = OSCresponder(server.addr,'/tr',{ arg time,responder,msg;
		if (msg[2] == mouseTrigID,{
		if ( msg[3] == 1, {
		{globalMouseDown = true;}.defer(0.05);
		},{
		{globalMouseDown = false;}.defer(0.05);
		});
		});
		}).add;
		}.defer(1);
		// defer
		{
		mouseButtonSynth = Synth("mouseButtonTrig");
		}.defer(2);
		}
	*/
	////////////////////////////////////////////////////////////////////////////////////
	*setModuleClipboard { arg moduleClass, argData;
		arrModuleClipboards.put(moduleClass.asString, argData);
	}

	*getModuleClipboard { arg moduleClass;
		^arrModuleClipboards.at(moduleClass.asString).deepCopy;
	}

	*getSynthArgSpec {arg argString;
		// Dummy method - here just used for snapshot no
		if (argString == "Snapshot", {^snapshotNo});
		^0;
	}
	*setSynthValue {
		// Dummy method
		^0;
	}

	*updateIPAddress {
		var holdFlag = NetAddr.broadcastFlag;
		NetAddr.broadcastFlag = true;
		OSCresponder(nil, '/getMyIP', { |t,r,msg,addr|
			dataBank.ipAddress = addr.ip;
			NetAddr.broadcastFlag = holdFlag;
			this.showView;
		}).add;
		NetAddr("255.255.255.255", NetAddr.langPort).sendMsg('/getMyIP');
	}

	*midiConnect {
		var inPorts = 8;
		var outPorts = 8;
		MIDIClient.init(inPorts,outPorts);			// explicitly intialize the client
		inPorts.do({ arg i;
			MIDIIn.connect(i, MIDIClient.sources.at(i));
		});
	}

	////////////////////////////////////////////////////////////////////////////////////

	*arrWidgetActionModules {
		// returns all modules including system itself that can perform actions for widgets
		// only selects modules where size of arrActionSpecs > 0
		^(	[TXSystem1]
			++ arrSystemModules.sort({ arg a, b;
				this.adjustNameForSorting(a.instName) < this.adjustNameForSorting(b.instName);
			})
			++ TXChannelRouting.arrChannels)
		.select({arg item, i; item.arrActionSpecs.size > 0; });
	}

	*arrWidgetValueActionModules {
		// returns all modules including system itself that can perform value actions for widgets
		// value actions are for continuous controls
		// only selects modules where size of arrActionSpecs > 0 and no of valueActions > 0
		^(	[TXSystem1]
			++ arrSystemModules.sort({ arg a, b;
				this.adjustNameForSorting(a.instName) < this.adjustNameForSorting(b.instName);
			})
			++ TXChannelRouting.arrChannels)
		.select({arg item, i; item.arrActionSpecs.size > 0; })
		.select({arg item, i; item.arrActionSpecs
			.select({arg action, i; action.actionType == \valueAction; }).size > 0;  });
	}

	*adjustNameForSorting {arg oldName;
		// adds zeros into instance no for correct sorting
		var index1, index2, holdZeros, newName;
		index1 = oldName.findBackwards("[");
		index2 = oldName.findBackwards("]");
		holdZeros = "0000".keep(5 - (index2-index1));
		newName = oldName.keep(index1+1) ++ holdZeros ++
		oldName.keep(1 - (oldName.size - index1));
		^newName;
	}

	*arrPatternEvents {
		// returns an array of events that can be used with Pbind patterns
		var holdArrModules, holdArrActionSpecs, holdArrAllEvents;
		holdArrAllEvents = (); // this is in itself an Event
		holdArrModules = this.arrWidgetActionModules;
		holdArrModules.do({arg argModule, i;
			var holdArrNumericalActionSpecs, holdArrModuleEvents;
			holdArrModuleEvents = [];
			holdArrNumericalActionSpecs = argModule.arrActionSpecs.select({arg action, i;
				(action.guiObjectType == \number and: (action.arrControlSpecFuncs.size > 0))
				or: (action.guiObjectType == \checkbox);
			});
			holdArrNumericalActionSpecs.do({arg argActionSpec, i;
				var holdEvent, arrValNames;
				holdEvent =  ( // new event
					moduleName: argModule.instName,
					actionName: argActionSpec.actionName,
					val1: 0,
					val2: 0,
					val3: 0,
					val4: 0,
					dur: 1,
					play: {
						var holdArrOldVals, holdArrNewVals;
						holdArrOldVals = [~val1,~val2, ~val3, ~val4];
						holdArrNewVals = nil!4;
						if (argActionSpec.guiObjectType == \number, {
							argActionSpec.arrControlSpecFuncs.do({arg argSpec, i;
								holdArrNewVals[i] = argSpec.value.constrain(holdArrOldVals[i])
							});
						},{
							holdArrNewVals[0] = ControlSpec(0, 1, step: 1).constrain(holdArrOldVals[0]);
						});
						if (argActionSpec.actionType == \commandAction, {
							argActionSpec.actionFunction.value(holdArrNewVals[0],holdArrNewVals[1],
								holdArrNewVals[2], holdArrNewVals[3]);
						},{
							argActionSpec.setValueFunction.value(holdArrNewVals[0]);
						});
					}
				);
				arrValNames = [\val1, \val2, \val3, \val4];
				argActionSpec.arrControlSpecFuncs.do({arg argSpecFunc, i;
					holdEvent[arrValNames[i]] = argSpecFunc.value.default;
				});
				holdArrModuleEvents = holdArrModuleEvents.add(holdEvent);

				// testing
				if (argModule.class == TXSystem1, {
					holdEvent.postcs;
					" ".postln;
				});

			});
			// end of holdArrNumericalActionSpecs.do
			holdArrAllEvents[argModule.moduleID] = holdArrModuleEvents;
		});
		// end of holdArrModules.do
		^holdArrAllEvents;
	}
	////////////////////////////////////////////////////////////////////////////////////

	*makeGui {
		var cmdPeriodFunc, holdScreenHeight, holdScreenWidth;

		holdScreenHeight = Window.screenBounds.height;
		holdScreenWidth = Window.screenBounds.width;

		w = Window("TX Modular System",
			Rect(0, holdScreenHeight - mainWindowHeight - 10, mainWindowWidth, mainWindowHeight),
			scroll: true);
		w.front;
		w.view.decorator = FlowLayout(w.view.bounds);
		w.view.background = dataBank.windowColour;
		w.alpha = dataBank.windowAlpha;
		w.acceptsMouseOver = true;
		this.setWindowImage;

		this.showView;

		// cmd-period action       -------to be added if needed--------
		//	cmdPeriodFunc = {
		// add any actions here
		//	 };
		//	CmdPeriod.add(cmdPeriodFunc);

		// when window closes remove cmdPeriodFunc.
		w.onClose = {
			//	CmdPeriod.remove(cmdPeriodFunc);
			this.closeSystem;
		};
	}

	*setWindowImage {
		if (GUI.current.asSymbol == \cocoa, {
			{	// if relevent, add background image
				if (dataBank.imageFileName.notNil and: {dataBank.displayModeIndex > 0}, {
					if (dataBank.holdImage == nil, {
						dataBank.holdImage = SCImage.open(TXPath.convert(dataBank.imageFileName));
					});
					w.view.backgroundImage_(dataBank.holdImage, dataBank.displayModeIndex);
				}, {
					w.view.backgroundImage_(SCImage.new(1), 1);
				});
			}.defer;
		});
	}


	/// SAVE AND LOAD SYSTEM /////////////////////////////////////////////////////////////////////////////////

	*saveData {
		// this method returns an array of all data for saving with various components:
		// 0- string "TXSystemSaveData", 1- this class, 2- holdNextModuleID, 3- arrAllModulesData,
		// 4- channelRoutingData, 5- arrBusData, 6 - arrSampleBanks, 7 - arrLoopBanks, 8 - notes
		// 9- arrFrontScreenData, 10- arrSnapshots, 11- arrModuleLayoutData, 12 - systemRevision
		// 13-background imageFileName, 14-background image displayModeIndex, 15-windowAlpha,
		// 16-windowColour.red, 17-windowColour.green, 18-windowColour.blue, 19-windowColour.alpha
		var arrData, arrAllModulesData, channelRoutingData, arrBusData, arrNotes, arrFrontScreenData, arrModuleLayoutData;
		// collect saveData from  all modules
		arrAllModulesData = arrSystemModules.collect({ arg item, i; item.saveData; });
		// collect data from TXChannelRouting
		channelRoutingData = TXChannelRouting.saveData;
		// collect bus data
		arrBusData = arrAllBusses.collect({ arg item, i; [item.moduleID, item.posX, item.posY]});
		// collect notes
		arrNotes = [notes0, notes1, notes2, notes3, notes4, notes5, notes6, notes7];
		// collect front screen data
		arrFrontScreenData = TXFrontScreen.saveData;
		// collect module layout data
		arrModuleLayoutData = TXSignalFlow.saveData;
		// build output
		arrData = ["TXSystemSaveData", this.class.asString, holdNextModuleID, arrAllModulesData,
			channelRoutingData, arrBusData, this.arrSampleBanks, this.arrLoopBanks, arrNotes,
			arrFrontScreenData, arrSnapshots, arrModuleLayoutData, systemRevision,
			dataBank.imageFileName, dataBank.displayModeIndex,dataBank.windowAlpha,
			dataBank.windowColour.red, dataBank.windowColour.green, dataBank.windowColour.blue,
			dataBank.windowColour.alpha,
		];
		^arrData;
	}

	*loadData { arg arrData;
		// this method updates all data by loading arrData. format:
		// 0- string "TXSystemSaveData", 1- this class, 2- holdNextModuleID, 3- arrAllModulesData,
		// 4- channelRoutingData, 5- arrBusData, 6 - arrSampleBanks, 7 - arrLoopBanks, 8 - notes
		// 9- arrFrontScreenData, 10- arrSnapshots, 11- arrModuleLayoutData, 12 - systemRevision
		// 13-background imageFileName, 14-background image displayModeIndex, 15-windowAlpha,
		// 16-windowColour.red, 17-windowColour.green, 18-windowColour.blue, 19-windowColour.alpha

		var arrAllModulesData, channelRoutingData, newModule, holdAutoOpen, holdModuleClass,
		arrBusData, arrNotes, arrFrontScreenData, windowLoading, arrModuleLayoutData;
		// error check
		if (arrData.class != Array, {
			TXInfoScreen.new("Error: invalid data. cannot load.");
			^0;
		});
		if (arrData.at(1) != this.class.asString, {
			TXInfoScreen.new("Error: invalid data class. cannot load.");
			^0;
		});

		// delete all modules in system
		this.emptySystem;
		// reset layout positions of all busses
		this.initBusPositions;

		// show loading window
		windowLoading = TXInfoScreen.new(
			"LOADING ... (this can take more than 10 secs)", 0, TXColour.orange);

		// temporarily store and turn off system.autoOpen while building system
		holdAutoOpen = autoOpen;
		autoOpen = false;

		// flag loading data
		dataBank.loadingDataFlag = true;

		// assign variables
		dataBank.savedSystemRevision = arrData.at(12) ? 0;
		holdNextModuleID = arrData.at(2).copy;
		arrAllModulesData = arrData.at(3).deepCopy;
		channelRoutingData = arrData.at(4).deepCopy;
		arrBusData = arrData.at(5).deepCopy;
		// check for older systems saved with single banks
		if (dataBank.savedSystemRevision >= 1000, {
			this.arrSampleBanks = arrData.at(6).deepCopy;
			this.arrLoopBanks = arrData.at(7).deepCopy;
		},{
			this.arrSampleBanks[0][0] = arrData.at(6).deepCopy;
			this.arrLoopBanks[0][0] = arrData.at(7).deepCopy;
		});
		arrNotes  = arrData.at(8) ? Array.newClear(8);
		arrFrontScreenData = arrData.at(9).deepCopy;
		arrSnapshots  = arrData.at(10).deepCopy ? Array.newClear(100);
		arrModuleLayoutData = arrData.at(11).deepCopy;
		if (arrModuleLayoutData.notNil,
			{TXSignalFlow.loadData(arrModuleLayoutData);
			});
		if (arrData.at(13).notNil, {
			dataBank.imageFileName = arrData.at(13);
			dataBank.holdImage = nil;
		});
		if (arrData.at(14).notNil, {
			dataBank.displayModeIndex = arrData.at(14) ? 1;
		});
		if (arrData.at(15).notNil, {
			dataBank.windowColour = Color(arrData.at(15), arrData.at(16),
				arrData.at(17), arrData.at(18));
		});

		this.setWindowImage;

		// set notes
		notes0 = arrNotes.at(0);
		notes1 = arrNotes.at(1);
		notes2 = arrNotes.at(2);
		notes3 = arrNotes.at(3);
		notes4 = arrNotes.at(4);
		notes5 = arrNotes.at(5);
		notes6 = arrNotes.at(6);
		notes7 = arrNotes.at(7);
		// set bus data
		arrAllBusses.do({ arg item, i;
			var holdData;
			holdData = arrBusData.at(i).asArray;
			item.moduleID = holdData.at(0);
			if (holdData.at(1).notNil, {
				item.posX = holdData.at(1);
			});
			if (holdData.at(2).notNil, {
				item.posY = holdData.at(2);
			});
		});
		// use routine to pause between modules
		Routine.run {
			var holdCondition, holdChanLoadQueue, holdLastChanCondition, totalWidgets;

			// for each saved module - recreate module, add to arrSystemModules and run loadData
			arrAllModulesData.do({ arg item, i;
				var holdModCondition;

				// add condition to load queue
				holdModCondition = holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				// pause
				this.server.sync;
				// run the Module's new method to get new instance of module
				holdModuleClass = item.at(1).interpret;
				newModule = holdModuleClass.new;
				// pause
				this.server.sync;
				// extra pause time
				newModule.extraLatency.wait;
				// add module to array of system modules
				arrSystemModules = arrSystemModules.add(newModule);
				// pause
				this.server.sync;
				// extra pause time
				newModule.extraLatency.wait;
				// load data into new module
				newModule.loadModuleID(item);
				newModule.loadData(item);

				// remove condition from load queue
				holdLoadQueue.removeCondition(holdModCondition);
			});

			// add condition to load queue
			holdCondition = holdLoadQueue.addCondition;
			// pause
			holdCondition.wait;
			// pause
			this.server.sync;
			//		1.wait;
			// remove condition from load queue
			holdLoadQueue.removeCondition(holdCondition);

			// add Load Queue for channels
			holdChanLoadQueue = holdLoadQueue;

			// create condition to go to end of channel load queue
			holdLastChanCondition = Condition.new(false);

			// load data to channelRouting & create channels
			TXChannelRouting.loadData(channelRoutingData, holdChanLoadQueue, holdLastChanCondition);

			// pause
			holdLastChanCondition.wait;
			// pause
			this.server.sync;
			// remove condition from load queue
			holdLoadQueue.removeCondition(holdLastChanCondition);

			// the following are extra inits for specific classes & modules:
			// restore all sequencer outputs
			(this.arrAllPossCurSeqModules ++ this.arrAllPossOldSeqModules).do ({ arg item, i;
				item.restoreAllOutputs;
			});
			// restore other outputs - on legacy modules
			TXAudioTrigger.restoreAllOutputs;
			TXAudioTrigger2.restoreAllOutputs;
			// restore outputs - on TXNoteStacker
			TXNoteStacker.restoreAllOutputs;
			// resync TXOSCRemote modules
			TXOSCRemote.syncAllModules;

			// restore system.autoOpen to original value
			autoOpen = holdAutoOpen;

			// add condition to load queue
			holdCondition = holdLoadQueue.addCondition;
			// pause
			holdCondition.wait;
			// pause
			this.server.sync;

			// remove condition from load queue
			holdLoadQueue.removeCondition(holdCondition);

			// load data to front screen
			totalWidgets = TXFrontScreen.loadData(arrFrontScreenData);
			if (totalWidgets > 1, {showFrontScreen = true; showWindow = "Run Interface";});

			// add condition to load queue
			holdCondition = holdLoadQueue.addCondition;
			// pause
			holdCondition.wait;

			// close loading window
			{windowLoading.close;}.defer;
			// reset flag loading data
			dataBank.loadingDataFlag = false;
			// rebuild required modules
			this.rebuildRequestedModules;

			// remove condition from load queue
			holdLoadQueue.removeCondition(holdCondition);

			// automatically set position data if missing
			if (arrModuleLayoutData.isNil, {
				TXSignalFlow.rebuildPositionData;
			});
			// update view
			this.showView;

		};
	}

	////////  SNAPSHOTS  ////////////////////////////////////////////////////////////

	*getSnapshotName { arg argSnapshotNo;
		var holdName, holdSnapshot;
		holdSnapshot = arrSnapshots.at(argSnapshotNo.asInteger);
		if (holdSnapshot.notNil, {holdName = holdSnapshot.at(0);}, {holdName = "";});
		^holdName;
	}

	*snapshotIsEmpty { arg argSnapshotNo;
		var holdSnapshot;
		holdSnapshot = arrSnapshots.at(argSnapshotNo.asInteger);
		if (holdSnapshot.isNil, {^true}, {^false});
	}

	*saveCurrentSnapshot { arg argSnapshotName;
		var holdSnapshot;
		snapshotName = argSnapshotName ? "";
		if (snapshotName == "", {snapshotName = "Snapshot " ++ snapshotNo.asString;});
		holdSnapshot = [snapshotName, this.saveSnapshotData].deepCopy;
		arrSnapshots.put(snapshotNo, holdSnapshot);
	}

	*overwriteCurrentSnapshot {
		var holdSnapshot, holdSnapshotName;
		holdSnapshotName = this.getSnapshotName(snapshotNo);
		holdSnapshot = [holdSnapshotName, this.saveSnapshotData].deepCopy;
		arrSnapshots.put(snapshotNo, holdSnapshot);
	}

	*saveSnapshotData {
		var arrData, arrAllModulesData, channelRoutingData;
		// collect saveData from  all modules
		arrAllModulesData = arrSystemModules.collect({ arg item, i; item.saveData; });
		// collect data from TXChannelRouting
		channelRoutingData = TXChannelRouting.saveData;
		// build output
		arrData = [nil, nil, nil, arrAllModulesData, channelRoutingData];
		^arrData;
	}

	*deleteCurrentSnapshot {
		arrSnapshots.put(snapshotNo, nil);
		snapshotName = "";
	}


	*loadSnapshot { arg argSnapshotNo;
		var holdSnapshot;
		holdSnapshot = arrSnapshots.at(argSnapshotNo.asInteger);
		snapshotNo = argSnapshotNo;
		if (holdSnapshot.isNil, {
			snapshotName = "";
		}, {
			snapshotName = holdSnapshot.at(0);
			this.loadSnapshotData(holdSnapshot.at(1));
		});
	}

	*loadSnapshotData { arg arrData;
		// this method updates all data by loading arrData. format:
		// 0- string "TXSystemSaveData", 1- this class, 2- holdNextModuleID, 3- arrAllModulesData,
		// 4- channelRoutingData, 5- arrBusData, 6 - sampleBank, 7 - loopBank, 8 - notes
		var arrAllModulesData, channelRoutingData, arrAllChannelData;

		arrAllModulesData = arrData.at(3).deepCopy;
		channelRoutingData = arrData.at(4).deepCopy;
		arrAllChannelData = channelRoutingData.at(3).deepCopy;

		// for each saved module -  run loadData
		arrAllModulesData.do({ arg item, i;
			var holdModule;
			// try to get module from ID. if found load data
			holdModule = this.getModuleFromID(item.at(2));
			if (holdModule != 0, {holdModule.loadData(item);});

		});
		// for each saved channel - load data
		arrAllChannelData.do({ arg item, i;
			var holdModule, holdArrSynthArgSpecs;
			// try to get module from ID. if found load synth args
			holdModule = this.getModuleFromID(item.at(2));
			if (holdModule != 0, {
				holdArrSynthArgSpecs = item.at(9).deepCopy;
				holdArrSynthArgSpecs.do({ arg holdSynthArgSpec, i;
					holdModule.setSynthValue(holdSynthArgSpec.at(0),holdSynthArgSpec.at(1));

				});
			});
		});

		// update view
		this.showView;

	} // end of method loadSnapshotData

	////////////////////////////////////////////////////////////////////////////////////

	*emptySystem {
		// stop all sequencers in system
		this.stopAllSyncModules;
		// clear system clock
		SystemClock.clear;
		// stop meters
		arrMeters.do({arg item, i; item.quit;});
		// deactivate any midi and keydown functions
		TXFrontScreen.midiDeActivate;
		TXFrontScreen.keyDownDeActivate;
		// delete all channels in  arrChannels in TXChannelRouting
		TXChannelRouting.deleteAllChannels;
		// delete all current modules in system
		arrSystemModules.size.do({ arg item, i; arrSystemModules[0].deleteModule;});
		// run inits
		TXBankBuilder2.initClass;
		TXWidget.initClass;
		TXFrontScreen.initClass;
		//	TXSeqGui.initClass;//	TXModGui.initClass;	snapshotNo = 0;
		snapshotName = "";
		this.clearHistory;
	}

	////////////////////////////////////////////////////////////////////////////////////

	*addModule {arg argModClass;
		var newModule, moduleIndex;
		if (server.serverRunning.not, {
			TXInfoScreen.new("Error: Server not running");
			^0;
		});
		// run the Module's new method to get new instance of module
		newModule = argModClass.new;
		// add module to array of system modules
		arrSystemModules = arrSystemModules.add(newModule);
		// if module type is source or groupsource or insert
		if ( (argModClass.moduleType == "source") or: (argModClass.moduleType == "groupsource")
			or: (argModClass.moduleType == "groupaction")
			or: (argModClass.moduleType == "insert"), {
				// set module index no in arrSystemModules
				moduleIndex = arrSystemModules.size - 1;
			});
		// if module type is Source or groupsource then add channel to routing
		if ((argModClass.moduleType == "source") or: (argModClass.moduleType == "groupsource"), {
			// set position
			TXSignalFlow.setPosition(newModule);
			TXChannelRouting.addChannel(newModule);
		});
		if (argModClass.moduleType == "action" or: (argModClass.moduleType == "groupaction"), {
			// set position
			TXSignalFlow.setPosition(newModule);
		});
		// post message
		//	("Adding Module: " ++ newModule.instName).postln;
		^newModule;
	}

	*nextModuleID {
		var outModuleID;
		outModuleID = holdNextModuleID;
		holdNextModuleID = holdNextModuleID + 1;
		^outModuleID;
	}

	*getModuleFromID {arg argModuleID;
		([this] ++ arrSystemModules ++ arrAllBusses ++ TXChannelRouting.arrChannels).do({ arg item, i;
			if (item.moduleID == argModuleID, { ^item; });
		});
		^0;
	}

	*checkDeletions {
		// if not closing down update screen
		if (closingDown == false, {
			// check TXChannelRouting for deletion effects
			TXChannelRouting.checkDeletions;
			// check historyEvents
			historyEvents = historyEvents.reject({arg item, i;
				(item.showWindow == "Modules & Channels")
				and: {item.displayModule.notNil}
				and: {item.displayModule.deletedStatus == true};
			});
			historyIndex = (historyEvents.size - 1);
			// check TXModGui for deletion effects
			//		TXModGui.checkDeletions;   		// run checkDeletions method on all system modules
			arrSystemModules.do({ arg item, i; item.checkDeletions; });
			// run checkDeletions method on all frontscreen widgets
			TXFrontScreen.checkDeletions;
			// delete any modules in arrSystemModules newly marked for deletion
			arrSystemModules.do({ arg item, i;
				if (item.toBeDeletedStatus==true, {
					item.deleteModule
				});
			});
			// recreate arrSystemModules without deleted ones
			arrSystemModules = arrSystemModules.select({ arg item, i; item.deletedStatus == false; });
			//  update screen
			this.showView;
		});
	}


	*requestModuleRebuild {arg argModule;
		// if system is loading data, then delay rebuilds
		if (dataBank.loadingDataFlag == false, {
			argModule.rebuildSynth;
		}, {
			dataBank.arrModulesForRebuilding.add(argModule);
		});
	}

	*rebuildRequestedModules {
		dataBank.arrModulesForRebuilding.do({ arg item, i;
			item.rebuildSynth;
		});
		// clear set
		dataBank.arrModulesForRebuilding = [].asSet;
	}

	*rebuildAllModules {
		// this method rebuilds all modules in case of module crashes:
		arrSystemModules.do({ arg item, i;
			item.rebuildSynth;
		});
	}

	*checkRebuilds {
		// check TXChannelRouting for rebuild effects
		TXChannelRouting.checkRebuilds;
	}

	*checkChannelsDest { arg argModule, argOptionNo;
		// check TXChannelRouting for destination change
		TXChannelRouting.checkChannelsDest(argModule, argOptionNo);
		//  update screen
		this.showView;
	}

	*closeSystem {
		// if standalone system then quit completely
		if (txStandAlone == 1, {
			0.exit;
		},{
			// set variables
			closingDown = true;
			holdFileName = "";
			// stop routines
			screenUpdRoutine.stop;
			{TXFrontScreenGuiProperties.closeWindow;}.defer;
			{TXHelpScreen.close;}.defer;
			// empty system
			this.emptySystem;
			//	NOTE - removed for now, can cause crashes
			//		// stop mouse synth
			//		mouseButtonSynth.free;
			//		mouseButtonResponder.remove;
			//	end
			//	server.freeAll;
			server.quit;
		});
	}
	*allNotesOff{
		// run method on all modules
		arrSystemModules.do({ arg item, i; item.allNotesOff; });
		// clear system clock
		SystemClock.clear;
	}

	*initBusPositions {
		var holdBusses;
		// Initialise module layout x/y positions of all busses
		holdBusses = arrFXSendBusses ++ arrAudioAuxBusses ++ arrMainOutBusses ++ arrControlAuxBusses;
		holdBusses.do({arg item, i; item.posX = 750; item.posY = (i+1) * 50;});
	}

	/// SAMPLES AND LOOPS //////////////////////////////////////////////////////////////////////////



	*arrSampleBanks{
		^TXBankBuilder2.arrSampleBanks;
	}

	*arrSampleBanks_ { arg argArrBanks;
		// set bank
		if (argArrBanks.notNil, {
			TXBankBuilder2.arrSampleBanks_(argArrBanks);
		});
	}
	*arrSampleBankNames{
		^TXBankBuilder2.arrSampleBanks.collect({arg item, i; i.asString + "-" + item[1]});
	}

	*arrLoopBanks{
		^TXBankBuilder2.arrLoopBanks;
	}

	*arrLoopBanks_ { arg argArrBanks;
		// set bank
		if (argArrBanks.notNil, {
			TXBankBuilder2.arrLoopBanks_(argArrBanks);
		});
	}
	*arrLoopBankNames{
		^TXBankBuilder2.arrLoopBanks.collect({arg item, i; i.asString + "-" + item[1]});
	}

	*sampleBank{ arg bankNo=0;
		// get bank
		^TXBankBuilder2.sampleBank(bankNo);
	}

	*sampleBank_ { arg argBank, bankNo=0;
		// set bank
		if (argBank.notNil, {
			TXBankBuilder2.sampleBank_(argBank, bankNo);
		});
	}

	*sampleFiles{ arg bankNo=0;
		// get bank
		^this.sampleBank(bankNo)[0];
	}

	*sampleFilesMono{ arg bankNo=0;
		// get bank
		^this.sampleFiles(bankNo).select({arg item, i; item.at(2) == 1;});
	}

	*sampleFilesStereo{ arg bankNo=0;
		// get bank
		^this.sampleFiles(bankNo).select({arg item, i; item.at(2) == 2;});
	}

	*sampleFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.sampleFiles(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}

	*sampleMonoFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.sampleFilesMono(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}

	*sampleStereoFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.sampleFilesStereo(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}
	*loopBank{ arg bankNo=0;
		// get bank
		^TXBankBuilder2.loopBank(bankNo);
	}

	*loopBank_ { arg argBank, bankNo=0;
		// set bank
		if (argBank.notNil, {
			TXBankBuilder2.loopBank_(argBank, bankNo);
		});
	}

	*loopFiles{ arg bankNo=0;
		// get bank
		^this.loopBank(bankNo)[0];
	}

	*loopFilesMono{ arg bankNo=0;
		// get bank
		^this.loopFiles(bankNo).select({arg item, i; item.at(2) == 1;});
	}

	*loopFilesStereo{ arg bankNo=0;
		// get bank
		^this.loopFiles(bankNo).select({arg item, i; item.at(2) == 2;});
	}

	*loopFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.loopFiles(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}

	*loopMonoFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.loopFilesMono(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}

	*loopStereoFileNames{ arg bankNo=0, cleanForPopup = false;
		// get bank
		^this.loopFilesStereo(bankNo).collect({arg item, i;
			var errorText, nameString;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			nameString = i.asString + "-" + errorText ++ item.at(0).basename;
			if (cleanForPopup == true, {
				nameString = TXString.removePopupSpecialCharacters(nameString);
			});
			nameString;
		});
	}

	////// SCREEN UPDATES //////////////////////////////////////////////////////////////////////////////

	*windowToFront {
		{w.front}.defer;
	}

	*addScreenUpdFunc { arg argArray, argScreenUpdFunc;
		// passed argument array and function to be valued
		arrScreenUpdFuncs = arrScreenUpdFuncs.add([argArray, argScreenUpdFunc]);
	}

	*runScreenUpdFuncs {

		// bypass globalMouseDown test for now
		//	// only run code if mouse is not down to stop crashes
		//	if (globalMouseDown == false, {

		if ((Main.elapsedTime - holdBootSeconds) > 0.5, {
			if (screenRebuild == true, {
				{ // defer function
					this.showViewAction;
					holdBootSeconds = Main.elapsedTime;
				}.defer;
			}, {
				// don't run if Design Interface shown
				if (showWindow != "Design Interface" and: {screenChanged == true}, {
					{ // defer function
						arrScreenUpdFuncs.do({arg item, i;
							var arrArgs = item.at(0);
							var holdFunc = item.at(1);
							holdFunc.value(arrArgs);
						});
					}.defer;
				});
			});
			screenRebuild = false;
			screenChanged = false;
		});

		//	});

	}

	*flagGuiUpd {
		screenChanged = true;
	}

	*clearScreenUpdFuncs {
		arrScreenUpdFuncs = [];
	}

	*flagGuiIfModDisplay {	arg argModule;
		// if argModule is currently being displayed on screen, then rebuild view
		if ((showWindow == "Modules & Channels") and: (TXChannelRouting.displayModule == argModule),
			{this.flagGuiUpd});
	}

	*showViewIfModDisplay {	arg argModule;
		// if argModule is currently being displayed on screen, then rebuild view
		if ((showWindow == "Modules & Channels") and: (TXChannelRouting.displayModule == argModule),
			{this.showView});
	}

	*isModDisplay {	arg argModule;
		var returnVal = false;
		// if argModule is currently being displayed on screen, then return true
		if ((showWindow == "Modules & Channels") and: (TXChannelRouting.displayModule == argModule),
			{returnVal = true;});
		^returnVal;
	}

	*showView {	// this schedules the view for update
		screenRebuild = true;
	}

	///////////////////////////////////////////////////////////////////////////////////
	*showViewAction {	// this creates the view
		var btnTitle, buttonLabels, sliderVol, btnVol;
		var btnHelp, btnLoadSystem, btnSaveSystem, btnNewSystem, btnRebuildSystem, btnCloseSystem;
		var btnAllNotesOff, popNewModule, btnAddModule, btnFrontBack, frontText, backText;
		var holdLeftVal, holdTopVal, btnBack, btnForward;
		var frontColour, backColour, frontTextColour, backTextColour, holdColor;
		var popMeters, holdMeter, txtArrow, btnTestNote, volumeSpec;

		// if not closing down update screen
		if (closingDown == false, {
			// wait for server sync
			// run Routine in AppClock
			Routine.run ({
				server.sync;
				// clear array
				this.clearScreenUpdFuncs;
				// deactivate any midi and keydown functions
				TXFrontScreen.midiDeActivate;
				TXFrontScreen.keyDownDeActivate;
				// set globalKeyDownAction
				View.globalKeyDownAction = { arg view,char,modifiers,unicode,keycode;
					TXFrontScreen.runKeyDownActionFunctions (char,modifiers,unicode,keycode);
				};
				// clear boxes
				if (headerBox.notNil, {
					this.deferRemoveView(headerBox);
				});
				if (viewBox.notNil, {
					this.deferRemoveView(viewBox);
				});
				w.view.decorator.reset;
				w.refresh;
				// if showSystemControls is on create headerBox to display system controls
				if (showSystemControls == 1, {
					// prepare to display header
					headerBox = CompositeView(w,Rect(0,0,1420,95));
					headerBox.decorator = FlowLayout(headerBox.bounds);

					// system title
					btnTitle = Button(headerBox,Rect(0,0,140,27))
					.font_(Font.new("Helvetica-Bold",16));
					btnTitle.states = [["TX Modular " ++ systemVersion, TXColor.sysGuiCol1,
						TXColor.white]];
					btnTitle.action = {
						"TX_Links".openHelpFile;
					};
					// space
					headerBox.decorator.shift(4, 0);
					// Row of system buttons
					// button - help
					btnHelp = Button(headerBox, 40 @ 27);
					btnHelp.states = [["Help", TXColor.white, TXColor.sysHelpCol]];
					btnHelp.action = {
						TXHelpScreen.open;
					};
					// button - Open file
					btnLoadSystem = Button(headerBox, 66 @ 27);
					btnLoadSystem.states = [["Open File", TXColor.white, TXColor.sysGuiCol2]];
					btnLoadSystem.action = {
						var newPath, newFile, newString, newData;
						if (server.serverRunning.not, {server.boot});
						Dialog.getPaths({ arg paths;
							// reload system settings
							this.loadSystemSettings;
							this.setWindowImage;
							newPath = paths.at(0);
							//	newFile = File(newPath,"r");
							//	newString = newFile.readAllString;
							//	newFile.close;
							newData = thisProcess.interpreter.executeFile(newPath);
							holdFileName = "    File name: " ++ newPath;
							this.loadData(newData);
							// post message
							("TX Opening File: " ++ newPath).postln;
						});
					};
					// button - save file
					btnSaveSystem = Button(headerBox, 66 @ 27);
					btnSaveSystem.states = [["Save File", TXColor.white, TXColor.sysGuiCol2]];
					btnSaveSystem.action = {
						var newPath, newFile, newData;
						Dialog.savePanel({ arg path;
							newPath = path;
							newData = this.saveData;
							newFile = File(newPath,"w");
							newFile << "#" <<< newData << "\n";
							//	use file as an io stream
							//	<<< means store the compile string of the object
							//	<< means store a print string of the object
							newFile.close;
							stTextFileName.string = "    File name: " ++ newPath.copy;
							holdFileName = "    File name: " ++ newPath.copy;
						});
					};
					// button - rebuild system
					btnRebuildSystem = Button(headerBox, 98 @ 27);
					btnRebuildSystem.states = [["Rebuild System", TXColor.white, TXColor.sysDeleteCol]];
					btnRebuildSystem.action = {
						this.rebuildAllModules;
					};
					// button - clear system
					btnNewSystem = Button(headerBox, 90 @ 27);
					btnNewSystem.states = [["Clear System", TXColor.white, TXColor.sysDeleteCol]];
					btnNewSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								this.emptySystem;
								this.loadSystemSettings;
								this.setWindowImage;
								holdFileName = " ";
								// reset layout positions of all busses
								this.initBusPositions;
								// update view
								this.showView;
							},
							"Are you sure you want to clear the system?"
						);
					};
					// button - Quit
					btnCloseSystem = Button(headerBox, 40 @ 27);
					btnCloseSystem.states = [["Quit", TXColor.white, TXColor.sysDeleteCol]];
					btnCloseSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								// if standalone system then quit completely else just close TX
								if (txStandAlone == 1, {
									0.exit;
								},{
									{w.close;}.defer;
								});
							},
							"Are you sure you want to quit?"
						);
					};
					// button - sync start
					Button(headerBox, 70 @ 27)
					.states_([
						["Sync Start", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						this.syncStart;
					});
					// button - sync stop
					Button(headerBox, 70 @ 27)
					.states_([
						["Sync Stop", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						this.syncStop;
					});
					// button - stop all sequencers
					Button(headerBox, 60 @ 27)
					.states_([
						["Stop All", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						this.stopAllSyncModules;
						this.showView;
					});
					// button - Panic! - all notes off
					btnAllNotesOff = Button(headerBox, 120 @ 27)
					.states_([["Panic! All Notes Off", TXColor.white, TXColor.sysDeleteCol]])
					.action_({ this.allNotesOff; });

					// test note button
					btnTestNote = Button(headerBox, 90 @ 27);
					btnTestNote.states = [["Play Test Note", TXColor.white, TXColor.sysGuiCol2]];
					btnTestNote.action = {
						{ (EnvGen.kr(Env.sine(2,1), 1.0, doneAction: 2)
							* Saw.ar(440, 0.1))!8
						}.play;
					};
					// popup - Meters
					popMeters = PopUpMenu(headerBox, 140 @ 27)
					.background_(TXColor.sysGuiCol2).stringColor_(TXColor.white);
					arrModulesForMeters = arrSystemModules
					.select({arg item, i; item.class.noOutChannels > 0 ;})
					.sort({arg item1, item2; item2.instName > item1.instName;});
					popMeters.items = ["Meters"]
					++ arrModulesForMeters.collect({ arg item, i; item.instName; })
					++ [	"Audio Out 1+2", "Audio Out 3+4", "Audio Out 5+6", "Audio Out 7+8",
						"Audio Out 9+10", "Audio Out 11+12", "Audio Out 13+14", "Audio Out 15+16",
						"Audio Out 1-4", "Audio Out 1-8", "Audio Out 1-16",
						"Audio In 1+2", "Audio In 3+4", "Audio In 5+6", "Audio In 7+8",
						"Audio In 1-4", "Audio In 1-8",
					] ++ (arrAudioAuxBusses ++ arrFXSendBusses ++ arrControlAuxBusses)
					.collect({ arg item, i; item.instName; });
					popMeters.action = {|view|
						var arrAllBusArrays, arrBusses, meterRate, arrBusRates, holdMethod;

						arrAllBusArrays = [ [] ]
						++ arrModulesForMeters.collect({
							arg item, i;
							var holdIndex, outArray;
							holdIndex = item.outBus.index;
							item.class.noOutChannels.do({arg item, i;
								outArray = outArray.add(holdIndex + i + 1);
							});
							outArray;
						})
						++ [
							[1,2], [3,4], [5,6], [7,8],
							[9,10], [11,12], [13,14], [15,16],
							(1..4), (1..8),(1..16),
							[1,2], [3,4], [5,6], [7,8],
							(1..4), (1..8),
						]
						++ (arrAudioAuxBusses ++ arrFXSendBusses ++ arrControlAuxBusses)
						.collect({ arg item, i; item.arrOutBusChoices.at(0).at(1) + 1; });
						arrBusses = arrAllBusArrays.at(view.value);

						arrBusRates = [\audio]
						++ arrModulesForMeters.collect({ arg item, i; item.class.moduleRate; })
						++ (\audio ! 17)
						++ (arrAudioAuxBusses ++ arrFXSendBusses ++ arrControlAuxBusses)
						.collect({ arg item, i; item.class.moduleRate; });

						meterRate = arrBusRates.at(view.value);

						if (arrBusses.size > 0, {
							case
							{ view.value > (17 + arrModulesForMeters.size) } {
								holdMeter = TXMeter.perform(
									'new', arrBusses-1, nil, nil, 10 @ 80,
									popMeters.items.at(view.value), meterRate
								);
							}
							{ view.value > (11 + arrModulesForMeters.size) } {
								holdMeter = TXMeter.perform(
									'input', arrBusses-1, nil, 10 @ 80,
									popMeters.items.at(view.value)
								);
							}
							{ view.value > arrModulesForMeters.size } {
								holdMeter = TXMeter.perform(
									'output', arrBusses-1, nil, 10 @ 80,
									popMeters.items.at(view.value)
								);
							}
							{ view.value > 0 } {
								holdMeter = TXMeter.perform(
									'new', arrBusses-1, nil, nil, 10 @ 80,
									popMeters.items.at(view.value), meterRate
								);
							}
							;
							// defer to allow meter synth to be built:
							if (holdMeter.notNil, {
								{holdMeter.autoreset = 3.0; holdMeter.rate = 20;}.defer(0.5); });
							arrMeters = arrMeters.add(holdMeter);
						});
						popMeters.value = 0;
					};
					// spacing
					headerBox.decorator.nextLine;
					headerBox.decorator.shift(0, 6);
					// text
					StaticText(headerBox, 90 @ 24)
					.string_("SYSTEM:")
					.background_(TXColor.sysLabelBackground)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');

					// display system buttons
					buttonLabels = ["Modules & Channels", "Signal Flow", "Sample Banks",
						"Loop Banks", "Notes & Options"];
					buttonLabels.do({arg item, i;
						var holdButton, holdBoxColour, holdTextColour;
						if (showWindow == item, {
							holdBoxColour = TXColor.white;
							holdTextColour = TXColor.sysGuiCol1;
						},{
							holdBoxColour = TXColor.sysGuiCol1;
							holdTextColour = TXColor.white;
						});
						holdButton = Button(headerBox, 122 @ 24);
						holdButton.states = [[item, holdTextColour, holdBoxColour]];
						holdButton.action = {
							showWindow = item;
							showFrontScreen = false;
							this.addHistoryEvent;
							this.showView;
						};
					});
					// spacing
					headerBox.decorator.shift(20, 0);
					// text
					StaticText(headerBox, 100 @ 24)
					.string_("INTERFACE:")
					.background_(TXColor.sysLabelBackground)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');

					// display Interface buttons
					buttonLabels = ["Run Interface", "Design Interface"];
					buttonLabels.do({arg item, i;
						var holdButton, holdBoxColour, holdTextColour;
						if (showWindow == item, {
							holdBoxColour = TXColor.white;
							holdTextColour = TXColor.sysInterfaceButton;
						},{
							holdBoxColour = TXColor.sysInterfaceButton;
							holdTextColour = TXColor.white;
						});
						holdButton = Button(headerBox, 122 @ 24);
						holdButton.states = [[item, holdTextColour, holdBoxColour]];
						holdButton.action = {
							showWindow = item;
							showFrontScreen = true;
							this.addHistoryEvent;
							this.showView;
						};
					});
					// spacing
					headerBox.decorator.shift(20, 0);
					// history buttons
					if (historyIndex > 0, {
						holdColor = TXColor.white;
					},{
						holdColor = TXColor.grey;
					});
					// button
					btnBack = Button(headerBox, 24 @ 24);
					btnBack.states = [["<", holdColor, TXColor.sysGuiCol1]];
					btnBack.action = {this.shiftHistory(-1);};
					if (historyIndex < (historyEvents.size - 1), {
						holdColor = TXColor.white;
					},{
						holdColor = TXColor.grey;
					});
					// button
					btnForward = Button(headerBox, 24 @ 24);
					btnForward.states = [[">", holdColor, TXColor.sysGuiCol1]];
					btnForward.action = {this.shiftHistory(1);};
					// spacing
					headerBox.decorator.nextLine;
					headerBox.decorator.shift(0, 4);
					//				if (GUI.current.asSymbol == \SwingGUI, {
					//					stTextFileName .font_(JFont("Gill Sans", 11));
					//				},{
					//					stTextFileName .font_(Font("Gill Sans", 11));
					//				});
					// static text - file name
					stTextFileName = StaticText(headerBox, 1298 @ 24)
					.background_(TXColor.white.alpha_(0.1))
					.string_(holdFileName)
					.stringColor_(TXColor.white);
					// spacing
					headerBox.decorator.shift(-154, 0);
					// keep vals
					holdLeftVal = headerBox.decorator.left;
					holdTopVal = headerBox.decorator.top;
					// spacing
					headerBox.decorator.shift(0, -70);
					// text
					StaticText(headerBox, 50 @ 27)
					.string_("Volume")
					.background_(TXColor.sysGuiCol2)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');

					// volume slider
					volumeSpec = [ -90, 6, \db].asSpec;
					sliderVol = Slider(headerBox, 90 @ 27)
					.background_(TXColor.sysGuiCol2)
					//	.align_(\right)
					.knobColor_(TXColor.white)
					.thumbSize_ (6)
					.value_(volumeSpec.unmap(dataBank.volume))
					.action_({arg view;
						var holdVol;
						holdVol = volumeSpec.map(view.value);
						dataBank.volume = holdVol;
						server.volume = holdVol;
					});

					// button
					btnVol = Button(headerBox, 34 @ 27);
					btnVol.states = [["0 dB", TXColor.white, TXColor.sysGuiCol2]];
					btnVol.action = {
						dataBank.volume = 0;
						server.volume = 0;
						sliderVol.value_(volumeSpec.unmap(0))
					};
				},{
					// system title
					btnTitle = Button(w,Rect(0,0,140,26))
					.font_(Font.new("Helvetica-Bold",16));
					btnTitle.states = [["TX Modular " ++ systemVersion, TXColor.sysGuiCol1,
						TXColor.white]];
					btnTitle.action = {
						"TX_Modular_Standalone_Links".openHelpFile;
					};
					// spacing
					w.view.decorator.shift(60, 0);
					// button - Quit
					btnCloseSystem = Button(w, 60 @ 27);
					btnCloseSystem.states = [["Quit", TXColor.white, TXColor.sysGuiCol1]];
					btnCloseSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								// if standalone system then quit completely else just close TX
								if (txStandAlone == 1, {
									0.exit;
								},{
									{w.close;}.defer;
								});
							},
							"Are you sure you want to quit?"
						);
					};
					// spacing
					w.view.decorator.shift(60, 0);
					// text
					StaticText(w, Rect(0,0, 60, 27))
					.string_("Volume")
					.background_(TXColor.sysGuiCol1)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');

					// volume slider
					volumeSpec = [ -90, 6, \db].asSpec;
					sliderVol = Slider(w, Rect(350,0, 250, 27))
					.background_(TXColor.white)
					//	.align_(\right)
					.knobColor_(TXColor.sysGuiCol1)
					.value_(volumeSpec.unmap(dataBank.volume))
					.action_({arg view;
						var holdVol;
						holdVol = volumeSpec.map(view.value);
						dataBank.volume = holdVol;
						server.volume = holdVol;
					});
					// button
					btnVol = Button(w, Rect(610,0, 30, 27));
					btnVol.states = [["<>", TXColor.white, TXColor.sysGuiCol1]];
					btnVol.action = {
						dataBank.volume = 0;
						server.volume = 0;
						sliderVol.value_(volumeSpec.unmap(0))
					};

				});	// end of headerBox creation

				// create viewBox to display selected window
				viewBox = CompositeView(w, Rect(0, 0, 2200, 1000));
				if (showFrontScreen == false, {
					viewBox.decorator = FlowLayout(viewBox.bounds);
				});
				if (showWindow == "Modules & Channels", {TXChannelRouting.makeGui(viewBox);});
				//	OLD			if (showWindow == "Modules & Channels", {TXModGui.makeGui(viewBox);});
				//	OLD			if (showWindow == "Sequencers", {TXSeqGui.makeGui(viewBox);});
				if (showWindow == "Signal Flow", {TXSignalFlow.makeGui(viewBox);});
				if (showWindow == "Sample Banks", {TXBankBuilder2.makeSampleGui(viewBox);});
				if (showWindow == "Loop Banks", {TXBankBuilder2.makeLoopGui(viewBox);});
				if (showWindow == "Notes & Options", {this.guiViewNotes});
				if (showFrontScreen == true, {TXFrontScreen.makeGui(viewBox, showWindow)});

				// make or close Gui Properties window
				if (showFrontScreen == true
					and: (showWindow == "Design Interface")
					and: (TXFrontScreen.classData.showGuiProperties == true)
					, {
						TXFrontScreenGuiProperties.makeGui(this);
				}, {
					TXFrontScreenGuiProperties.closeWindow;
				});
			}, clock: AppClock); // end of Routine.run
		}); // end of if
	} // end of method showViewAction

	*deferRemoveView {arg holdView;
		if (holdView.notNil, {
			if (holdView.notClosed, {
				holdView.visible_(false);
				holdView.focus(false);
				{holdView.remove}.defer(1);
			});
		});
	}
	*addImageDialog  {
		var holdString;
		// get path/filenames
		Dialog.getPaths({ arg paths;
			var holdFile;
			holdFile = SCImage.open(paths[0]);
			if (holdFile.isNil, {
				TXInfoScreen.new(
					"Error: the following is not a valid image files:",
					arrInfoLines: [paths[0]]
				);
			},{
				//
				dataBank.imageFileName = paths[0];
				dataBank.holdImage = holdFile;
				this.setWindowImage;
				this.saveSystemSettings;
			});
			// recreate view
			this.showView;
		}, nil, false);
	}
	// history /////////////////////////////////////////////////////////////////////////////////

	*shiftHistory {arg shiftVal;
		var newIndex, holdEvent;
		newIndex = (historyIndex + shiftVal).max(0).min((historyEvents.size - 1).max(0));
		if (historyIndex != newIndex, {
			historyIndex = newIndex;
			holdEvent = historyEvents[historyIndex];
			showWindow = holdEvent.showWindow;
			showFrontScreen = holdEvent.showFrontScreen;
			if (showWindow == "Modules & Channels", {
				TXChannelRouting.displayModule = holdEvent.displayModule;
				TXChannelRouting.showModuleBox = holdEvent.showModuleBox;
			});
			if (showFrontScreen == true, {
				TXFrontScreen.storeCurrLoadNewLayer(holdEvent.layerNo);
			});
			this.showView;
		});
	}
	*clearHistory {
		historyEvents = [];
		historyIndex = 0;
	}
	*addHistoryEvent {
		var holdEvent;
		holdEvent = ();
		holdEvent.showWindow = showWindow;
		holdEvent.showFrontScreen = showFrontScreen;
		holdEvent.displayModule = TXChannelRouting.displayModule;
		holdEvent.showModuleBox = TXChannelRouting.showModuleBox;
		holdEvent.layerNo = TXFrontScreen.classData.layerNo;
		if (historyEvents[historyIndex] != holdEvent, {
			// remove any history after current index before adding
			historyEvents = historyEvents.keep(historyIndex + 1);
			historyEvents = historyEvents.add(holdEvent);
		});
		historyEvents = historyEvents.keep(-10);
		historyIndex = historyEvents.size - 1;
	}
	///////////////////////////////////////////////////////////////////////////////////

	*guiViewNotes {
		var noteView, btnUpdateIP, btnDefault, screenColourBox, screenColourPopup, holdView;
		var arrAllSourceActionModules, arrAllSourceActionModNames, modulesScrollView, modulesBox, btnDelete;
		var modListBox, modListBoxWidth, modListBoxHeight;
		var displayModePopupView, displayModeNumberView, displayModeItems;
		var addImageButton, delImageButton;
		var imageNameText;

		arrAllSourceActionModules = arrSystemModules
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "groupaction")
			or:
			(item.class.moduleType == "action")
			or:
			(item.class.moduleType == "insert") ;
		})
		.sort({ arg a, b;
			this.adjustNameForSorting(a.instName) < this.adjustNameForSorting(b.instName);
		});
		arrAllSourceActionModNames = arrAllSourceActionModules
		.collect({arg item, i;  item.instName; });
		//  spacer
		viewBox.decorator.shift(40,0);
		// create note fields with titles
		noteView =  CompositeView(viewBox,Rect(0,0,1200,800));
		noteView.decorator = FlowLayout(noteView.bounds);
		//  spacer
		noteView.decorator.shift(0,20);
		// text
		StaticText(noteView, 100 @ 30).string_("OPTIONS").align_(\centre)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);

		/* IMMAGE REMOVED FOR NOW
		// label - background image
		StaticText(noteView, Rect(0, 0, 120, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Background Image" );
		// text - image file name
		imageNameText = StaticText(noteView, Rect(0, 0, 300, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\left);
		imageNameText.string = (dataBank.imageFileName ? " ").keep(60);
		// button - add image
		addImageButton = Button(noteView, Rect(0, 0, 80, 20));
		addImageButton.states = [
			["Add Image", TXColor.white, TXColour.sysGuiCol1]
		];
		addImageButton.action = {this.addImageDialog;};
		// button - delete image
		delImageButton = Button(noteView, Rect(0, 0, 80, 20));
		delImageButton.states = [
			["Delete Image", TXColor.white, TXColour.black]
		];
		delImageButton.action = {
			dataBank.imageFileName = nil;
			dataBank.holdImage = nil;
			this.setWindowImage;
			imageNameText.string = " ";
			this.saveSystemSettings;
		};
		// label -  image mode
		StaticText(noteView, Rect(0, 0, 90, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Image mode" );
		//display mode
		displayModeItems = [
			"0 - off - image not shown, show window colour",
			"1 - fix left, fix top - default",
			"2 - tile horizontally, fix top",
			"3 - fix right, fix top",
			"4 - fix left, tile vertically",
			"5 - tile horizontally, tile vertically",
			"6 - fix right, tile vertically",
			"7 - fix left, fix bottom",
			"8 - tile horizontally, fix bottom",
			"9 - fix right, fix bottom",
			"10 - stretch horizontally & vertically to fit",
			"11 - center horizontally , center vertically & scale",
			"12 - center horizontally , fix top",
			"13 - center horizontally , fix bottom",
			"14 - fix left, center vertically",
			"15 - fix right, center vertically",
			"16 - center horizontally, center vertically - no scale",
		];
		// number box - display mode
		displayModeNumberView = NumberBox(noteView, Rect(0, 0, 20, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.scroll_(false)
		.action_({arg view;
			var holdValue;
			holdValue = view.value.clip(0,16);
			dataBank.displayModeIndex = holdValue;
			this.saveSystemSettings;
			this.setWindowImage;
			// update view
			this.showView;
		});
		displayModeNumberView.value = dataBank.displayModeIndex;
		// popup - display mode
		displayModePopupView = PopUpMenu(noteView, Rect(0, 0, 20, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(displayModeItems)
		.action_({arg view;
			dataBank.displayModeIndex = view.value;
			this.saveSystemSettings;
			this.setWindowImage;
			// update view
			this.showView;
		});
		displayModePopupView.value = dataBank.displayModeIndex;
		*/

		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 120 @ 24)
		.string_("Window Colour")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		// screenColourbox
		screenColourBox = DragBoth.new(noteView, 34 @ 24);
		screenColourBox.background_(w.view.background);
		screenColourBox.beginDragAction_({ arg view, x, y;
			view.dragLabel_("Colour");
			screenColourBox.background;
		});
		screenColourBox.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Color )
		};
		screenColourBox.receiveDragHandler = {
			var holdDragObject;
			holdDragObject = View.currentDrag;
			w.view.background = holdDragObject;
			dataBank.windowColour = holdDragObject;
			screenColourBox.background_(holdDragObject);
			this.saveSystemSettings;
			// update view
			w.refresh;
		};
		// button
		btnDefault = Button(noteView, 60 @ 24);
		btnDefault.states = [["Default", TXColor.white, TXColor.sysMainWindow]];
		btnDefault.action = {
			w.view.background = TXColour.sysMainWindow;
			screenColourBox.background_(w.view.background);
			dataBank.windowColour = w.view.background;
			this.saveSystemSettings;
		};

		// colourPickerButton
		Button(noteView, 60 @ 24)
		.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			TXColour.showPicker;
		});
		// popup - screenColour presets
		screenColourPopup = PopUpMenu(noteView, 140 @ 24)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(["Presets"] ++ TXColour.colourNames)
		.action_({arg view;
			if (view.value > 0, {
				w.view.background =
				TXColour.perform(TXColour.colourNames.at(view.value - 1).asSymbol).copy;
				screenColourBox.background_(w.view.background);
				dataBank.windowColour = w.view.background;
				this.saveSystemSettings;
			});
		});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 140 @ 24)
		.string_("Window Transparancy")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		// buttons
		6.do({ arg i;
			var btn, col1, col2;
			if (dataBank.windowAlpha == (1 - (0.1 * i)), {
				col2 = TXColor.white;
				col1 = TXColor.sysGuiCol1;
			},{
				col1 = TXColor.white;
				col2 = TXColor.sysGuiCol1;
			});
			btn = Button(noteView, 50 @ 24);
			btn.states = [[["0 %", "10 %", "20 %", "30 %", "40 %", "50 %"][i], col1, col2]];
			btn.action = {
				w.alpha = 1 - (0.1 * i);
				dataBank.windowAlpha = 1 - (0.1 * i);
				this.saveSystemSettings;
				this.showView;
			};
		});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 360 @ 24)
		.string_("Ask for confirmation before deleting Modules and Channels?")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TXCheckBox(noteView, 70 @ 24, "", TXColor.sysGuiCol1, TXColour.grey(0.8),
			TXColor.white, TXColor.sysGuiCol1, 10)
		.action_({arg view; dataBank.confirmDeletions = view.value.booleanValue;
			this.saveSystemSettings;})
		.value_(dataBank.confirmDeletions.binaryValue;);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 40);
		// main title
		StaticText(noteView, 100 @ 30).string_("NOTES").align_(\centre)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		//  display Network  info
		StaticText(noteView, 500 @ 24)
		.string_("Network IP address for receiving OSC messages:  "
			++ dataBank.ipAddress.asString ++
			"      Network Port:  " ++ NetAddr.langPort.asString)
		.align_(\center)
		.background_(TXColor.sysChannelHighlight)
		.stringColor_(TXColor.sysGuiCol1);
		//	// button
		//	btnUpdateIP = Button(noteView, 140 @ 24);
		//	btnUpdateIP.states = [["Update Network Info", TXColor.white, TXColor.sysGuiCol1]];
		//	btnUpdateIP.action = {
		//		this.updateIPAddress;
		//	};
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		//  display all notes
		StaticText(noteView, 60 @ 24).string_("Notes 1").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes0)
		.action_({arg view; notes0 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 2").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes1)
		.action_({arg view; notes1 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 3").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes2)
		.action_({arg view; notes2 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 4").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes3)
		.action_({arg view; notes3 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 5").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes4)
		.action_({arg view; notes4 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 6").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes5)
		.action_({arg view; notes5 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 7").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes6)
		.action_({arg view; notes6 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 24).string_("Notes 8").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 24)
		.string_(notes7)
		.action_({arg view; notes7 = view.value;});

		/* new code for multiple deletions. Not currently needed since moved to modules page

			//  decorator.reset
			noteView.decorator.reset;
			noteView.decorator.shift(800,20);

			// make box
			modListBoxWidth = 250;
			modListBoxHeight = 600;
			modListBox =  CompositeView(noteView, Rect(0,0, modListBoxWidth, modListBoxHeight));
			modListBox.background = TXColour.sysChannelAudio;
			modListBox.decorator = FlowLayout(modListBox.bounds);
			// Heading
			holdView = StaticText(modListBox, Rect(0,0, 168, 30));
			holdView.string = "All System Modules";
			holdView.stringColor_(TXColour.sysGuiCol4).background_(TXColor.white);
			holdView.setProperty(\align,\center);

			modListBox.decorator.nextLine;
			modListBox.decorator.shift(0, 10);

			modulesScrollView = ScrollView(modListBox, Rect(0,0, modListBoxWidth-8, modListBoxHeight-38))
			.hasBorder_(false).autoScrolls_(false);
			modulesScrollView.action = {
			arg view; dataBank.modulesVisibleOrigin = view.visibleOrigin;
			};
			modulesBox = CompositeView(modulesScrollView,
			Rect(0,0, modListBoxWidth-14, (arrAllSourceActionModNames.size * 30).max(20)));
			modulesBox.decorator = FlowLayout(modulesBox.bounds);

			arrAllSourceActionModNames.do({arg item, i;
			var strModule, btnDelete;
			// button -  module
			strModule = StaticText(modulesBox, 140 @ 20);
			strModule.string = item;
			strModule.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			strModule.setProperty(\align,\center);
			// button -  delete
			btnDelete = Button(modulesBox, 24 @ 20);
			btnDelete.states = [["Del", TXColor.white, TXColor.sysDeleteCol]];
			btnDelete.action = {arrAllSourceActionModules.at(i).confirmDeleteModule; this.showView;};
			});
			modulesScrollView.visibleOrigin = dataBank.modulesVisibleOrigin;
		*/

	}

}
