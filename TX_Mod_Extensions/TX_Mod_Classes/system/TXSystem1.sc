// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSystem1 {		// system module 1

	// Note: GUI has been moved to TXSystem1GUI

	/*	 use the following code to show all class methods:
		TXSystem1.class.dumpMethodList;
	*/
	////////////////////////////////////////////////////////////////////////////////////
	//	define class variables:

	classvar <>dataBank;					// event to hold data

	classvar <arrAllPossModules;			// array of all possible modules.
	classvar <arrAllPossCurSeqModules;		// array of all possible current sequencer modules.
	classvar <arrSystemModules;				// array of modules in system.
	classvar <arrModuleClipboards;			// array of modules clipboards for copying/pasting.
	classvar <arrMainOutBusses;				// array of main out busses
	classvar <arrFXSendBusses;				// array of FX send busses
	classvar <arrAudioAuxBusses;			// array of Audio Aux busses
	classvar <arrControlAuxBusses;			// array of Control Aux busses
	classvar <arrAllBusses;					// single array of all busses used

	classvar <server;						// server variable
	classvar <moduleID = 99;				// system itself has a moduleID of 99;
	classvar <groupModules, <groupChannels;	// groups for order of execution
	classvar <>autoOpen = false;
	classvar <>autoRun = true;
	classvar <>latency = 0.1;				// general latency time in seconds to allow server to do something
	classvar <instName = "System";			// system name
	classvar <instDisplayName = "System";	// system name
	classvar <arrActionSpecs;				// specs for system actions available to widgets
	classvar <guiSpecTitleArray;
	classvar <>showFrontScreen;
	classvar <showSystemControls;
	classvar <arrSnapshots;					// array of system snapshots
	classvar <holdLoadQueue;
	classvar <defSampleRate = 44100;		//	default sample rate. (also set in TXModuleBase)
	classvar <txStandAlone = 0;
	classvar <>showWindow;					// for gui
	classvar <arrPresets;					// dummy array of presets

	// bypass globalMouseDown test for now, can cause crashes
	//classvar <globalMouseDown;			// mouse button variables:
	//classvar mouseButtonResponder;
	//classvar mouseButtonSynth;

	////////////////////////////////////////////////////////////////////////////////////
	// Define class methods

	*initClass{
		// create event and set variables:
		dataBank = ();
		dataBank.systemVersion = "087";					// version of the TX Modular system shown in gui
		dataBank.standaloneName = "TX_Modular_087s";
		dataBank.systemRevision = 1004;					// current revision no of the system

		dataBank.savedSystemRevision = 0;
		//dataBank.modulesVisibleOrigin = Point.new(0,0);
		dataBank.holdBootSeconds = Main.elapsedTime;
		dataBank.windowVisibleOrigin = Point.new(0,0);
		dataBank.mainWindowWidth = 1410;
		dataBank.mainWindowHeight = 786;
		dataBank.viewBoxWidth = 3000;
		dataBank.viewBoxHeight = 1500;
		dataBank.closingDown = false;	// only used when closing down
		dataBank.screenChanged = false;	// for gui
		dataBank.screenRebuild = false;	// for gui
		dataBank.holdFileName = "";		// for gui
		dataBank.defaultChanLevel = 0;
		dataBank.screenUpdateRate = 5;     // default
		dataBank.screenUpdPauseTime = 0.1; // default
		dataBank.arrScreenUpdFuncs = [];
		dataBank.snapshotNo = 0;
		dataBank.snapshotName = "";
		dataBank.audioSetupQuit = false;	// for final closing down
		dataBank.seqLatency = 0.1;        // latency used by sequencers

		Font.default =  Font("Helvetica", 12);

		this.buildArrays;
	}

	*seqLatency {
		^dataBank.seqLatency;
	}

	*buildArrays {
		// array of all possible modules
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
			TXAmpComp,
			TXAmpCompSt,
			TXAmpSim,
			TXAmpSimSt,
			TXAnalyser3,
			TXAnimateCode2,
			TXAudioIn4,
			TXAudioTrigger3,
			TXBall,
			TXBalance,
			TXBalanceSt,
			TXBitCrusher2,
			TXBitCrusher2St,
			TXBowed,
			TXBowedRes,
			TXBracketEQ,
			TXBracketEQSt,
			TXChangedTrig,
			TXChorus,
			TXChorusSt,
			TXClockPulse,
			TXCodeInsertAu,
			TXCodeInsertAuSt,
			TXCodeInsertCtrl,
			TXCodeSourceAu,
			TXCodeSourceAuSt,
			TXCodeSourceCtrl,
			TXCompander3,
			TXCompander3St,
			TXControlDelay4,
			TXConvertToControl,
			TXConvertToAudio,
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
			TXDistortion3,
			TXDistortion3St,
			TXDualFilter,
			TXDualFilterSt,
			TXEnv16Stage,
			TXEnvCurve,
			TXEnvDADSR4,
			TXEnvDADSSR,
			TXEnvFollow,
			TXEnvFollowSt,
			TXEQ6,
			TXEQ6St,
			TXEQ6M,
			TXEQ6MSt,
			TXEQGraphic,
			TXEQGraphicSt,
			TXEQMorph,
			TXEQMorphSt,
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
			TXFMDrone,
			TXFMSynth5,
			TXFormlets,
			TXGain,
			TXGain16Stage,
			TXGain16StageSt,
			TXGainCurve,
			TXGainCurveSt,
			TXGainDADSR,
			TXGainDADSRSt,
			TXGainDADSSR,
			TXGainDADSSRSt,
			TXGainSt,
			TXGameTrak,
			TXGaussClock,
			TXGranulator2,
			TXGranulatorFM,
			TXGreyhole,
			TXGreyholeSt,
			TXGroupMorph,
			TXHarmoniser,
			TXInfinity,
			TXInfinitySt,
			TXInterference,
			TXInterferenceSt,
			TXKlank,
			TXJPverb,
			TXJPverbSt,
			TXLFOMulti2,
			TXLFOCurve,
			TXLimiter,
			TXLimiterSt,
			TXLiveGrain,
			TXLiveLooper2,
			TXLogicBool,
			TXLogicNonBin,
			TXLoopPlayer,
			TXLoopPlayerSt2,
			TXMatrixAudio8x8,
			TXMatrixControl8x8,
			TXMembrane,
			TXMIDIController2,
			TXMIDIControlOut3,
			TXMIDINote2,
			TXMIDIOut,
			TXMIDIPitchbend2,
			TXMIDIVelocity2,
			TXMixAudio8to1,
			TXMixAudio16to2,
			TXMixControl8to1,
			TXModSequencer,
			TXMonoToStereo,
			TXMidSideDecoder,
			TXMidSideEncoder,
			TXMonoEnv16Stage,
			TXMonoEnvCurve,
			TXMonoEnvDADSR,
			TXMonoEnvDADSSR,
			TXMultiTapDelay2,
			TXMultiTrackSeq,
			TXNoiseGate,
			TXNoiseGateSt,
			TXNoiseWhitePink,
			TXNormalizer,
			TXNormalizerSt,
			TXNotchPhaser,
			TXNotchPhaserSt,
			TXNoteMultiply,
			TXNoteStacker,
			TXOnOffSwitch,
			TXOSCController,
			TXOSCController2D,
			TXOSCControlOut,
			TXOSCOut,
			TXOSCRemote,
			TXOSCTrigger,
			TXPatternCode,
			TXPerlinNoise,
			TXPhaser,
			TXPhaserSt,
			TXPianoResSt,
			TXPianoStringSt,
			TXPItchFollower4,
			TXPitchShifter,
			TXPitchShifterSt,
			TXPingPong,
			TXPingPongSt,
			TXPluckSynth,
			TXPulseDivider,
			TXPulseDividerV,
			TXReverb2,
			TXReverbSt2,
			TXReverbA,
			TXReverbF,
			TXReverbFSt,
			TXReverbG,
			TXRingMod2,
			// TXQCParticles2, // not working
			// TXQCPlayer4, // not working
			TXQuantise,
			TXSampleHold,
			TXSamplePlayer5a,
			TXSamplePlayerSt6,
			TXSamplePlayerPlus3,
			TXSamplePlayerPlus3St,
			TXScaleChord,
			TXScaleQuantise,
			TXSimpleSlider2,
			TXSlope,
			TXSmooth2,
			TXSoftKneeComp,
			TXSoftKneeCompSt,
			TXSpectralFX,
			TXSpectralDelay,
			TXSpring,
			TXStepSequencer,
			TXStereoToMono2,
			TXStereoWidth,
			TXStrings,
			TXSubHarm,
			TXSubHarmSt,
			TXTableDrone,
			TXTableDroneSt,
			TXTableSynth5,
			TXToggleSwitch,
			TXTNoiseRing,
			TXTRandom,
			TXTRandomChoice,
			TXTRandomWalk,
			TXTransientShape,
			TXTransientShapeSt,
			TXTrig2Gate,
			TXTrigImpulse,
			TXTubes,
			//TXV_System, // not working
			TXVocoder2,
			TXVocoderFX2,
			TXVosim,
			TXVowelMorph,
			TXVowelMorphSt,
			TXWarp2,
			TXWarpMorph,
			TXWaveFold,
			TXWaveFoldSt,
			TXWaveform5,
			TXWaveform5St,
			TXWaveshaper,
			TXWaveshaperSt,
			TXWaveshaperX,
			TXWaveshaperXSt,
			TXWaveSynth8,
			TXWaveSynthPlus4,
			TXWaveTerrain,
			TXWiiController,
			TXWiiControllerOSC3,
			TXWiiTrigger,
			TXWiiTrigOSC2,
			TXXDistort,
			TXXDistortSt,
			TXXFader2to1,
			TXXFader2to1C,
			TXXFader4to2,
		];
		// the following strings are formatted as popup item with categories and modules
		dataBank.arrSourceModulesByCategory = [
			["Audio: Conversion", "Convert To Audio", TXConvertToAudio],
			["Audio: Drone", "FM Drone", TXFMDrone],
			["Audio: Drone", "Noise White-Pink", TXNoiseWhitePink],
			["Audio: Drone", "Table Drone", TXTableDrone],
			["Audio: Drone", "Table Drone St", TXTableDroneSt],
			["Audio: Drone", "Vosim", TXVosim],
			["Audio: Drone", "Vowel Morph", TXVowelMorph],
			["Audio: Drone", "Vowel Morph St", TXVowelMorphSt],
			["Audio: Drone", "Waveform ", TXWaveform5],
			["Audio: Drone", "Waveform St", TXWaveform5St],
			["Audio: Drone", "Wave Terrain", TXWaveTerrain],
			["Audio: File Player", "File Player ", TXFilePlayer6],
			["Audio: File Player", "File Player St", TXFilePlayer6St],
			["Audio: Inputs", "Audio Inputs", TXAudioIn4],
			["Audio: Mid-Side Encoding", "M-S Decoder", TXMidSideDecoder],
			["Audio: Mid-Side Encoding", "M-S Encoder", TXMidSideEncoder],
			["Audio: Mixing", "Matrix A 8x8 ", TXMatrixAudio8x8],
			["Audio: Mixing", "Mix Audio 8-1 ", TXMixAudio8to1],
			["Audio: Mixing", "Mix Audio 16-2", TXMixAudio16to2],
			["Audio: Mixing", "X-Fader 2-1", TXXFader2to1],
			["Audio: Mixing", "X-Fader 4-2", TXXFader4to2],
			["Audio: Polyphonic", "Bowed", TXBowed],
			["Audio: Polyphonic", "Filter Synth", TXFilterSynth2],
			["Audio: Polyphonic", "FM Synth", TXFMSynth5],
			["Audio: Polyphonic", "Granulator", TXGranulator2],
			["Audio: Polyphonic", "Granulator FM", TXGranulatorFM],
			["Audio: Polyphonic", "Loop Player", TXLoopPlayer],
			["Audio: Polyphonic", "Loop Player St", TXLoopPlayerSt2],
			["Audio: Polyphonic", "Piano String St", TXPianoStringSt],
			["Audio: Polyphonic", "Pluck Synth", TXPluckSynth],
			["Audio: Polyphonic", "Sample Player", TXSamplePlayer5a],
			["Audio: Polyphonic", "Sample Player St", TXSamplePlayerSt6],
			["Audio: Polyphonic", "Sample Player+ ", TXSamplePlayerPlus3],
			["Audio: Polyphonic", "Sample Player+ St", TXSamplePlayerPlus3St],
			["Audio: Polyphonic", "Table Synth", TXTableSynth5],
			["Audio: Polyphonic", "Wave Synth", TXWaveSynth8],
			["Audio: Polyphonic", "Wave Synth+", TXWaveSynthPlus4],
			["Audio: SuperCollider", "Code Source A", TXCodeSourceAu],
			["Audio: SuperCollider", "Code Source A St", TXCodeSourceAuSt],
			["Control: Analysis", "Amp Follower", TXAmpFollower3],
			["Control: Analysis", "Analyser", TXAnalyser3],
			["Control: Analysis", "Audio Trigger", TXAudioTrigger3],
			["Control: Analysis", "CyclOSC Colour", TXCyclOSCCol],
			["Control: Analysis", "CyclOSC Grey", TXCyclOSCGrey],
			["Control: Analysis", "Pitch Follower", TXPItchFollower4],
			["Control: Clock & Divider", "Clock Pulse", TXClockPulse],
			["Control: Clock & Divider", "Gauss Clock", TXGaussClock],
			["Control: Clock & Divider", "Pulse Divider", TXPulseDivider],
			["Control: Clock & Divider", "Pulse DividerV", TXPulseDividerV],
			["Control: Conversion", "Convert To Control", TXConvertToControl],
			["Control: Envelopes Mono", "Mono Env 16-stage", TXMonoEnv16Stage],
			["Control: Envelopes Mono", "Mono Env Curve", TXMonoEnvCurve],
			["Control: Envelopes Mono", "Mono Env DADSR", TXMonoEnvDADSR],
			["Control: Envelopes Mono", "Mono Env DADSSR", TXMonoEnvDADSSR],
			["Control: Envelopes Poly", "Env 16-stage", TXEnv16Stage],
			["Control: Envelopes Poly", "Env Curve", TXEnvCurve],
			["Control: Envelopes Poly", "Env DADSR", TXEnvDADSR4],
			["Control: Envelopes Poly", "Env DADSSR", TXEnvDADSSR],
			["Control: Envelopes Poly", "Trigger Impulse", TXTrigImpulse],
			["Control: Input Device", "Gametrak", TXGameTrak],
			["Control: Input Device", "Wii Ctrl Darwiin", TXWiiController],
			["Control: Input Device", "Wii Ctrl OSC", TXWiiControllerOSC3],
			["Control: Input Device", "Wii Trig Darwiin", TXWiiTrigger],
			["Control: Input Device", "Wii Trig OSC", TXWiiTrigOSC2],
			["Control: Logic", "Logic Bool", TXLogicBool],
			["Control: Logic", "Logic NonBin", TXLogicNonBin],
			["Control: MIDI", "MIDI Controller", TXMIDIController2],
			["Control: MIDI", "MIDI Control Out", TXMIDIControlOut3],
			["Control: MIDI", "MIDI Note", TXMIDINote2],
			["Control: MIDI", "MIDI Out ", TXMIDIOut],
			["Control: MIDI", "MIDI Pitchbend", TXMIDIPitchbend2],
			["Control: MIDI", "MIDI Velocity", TXMIDIVelocity2],
			["Control: Mixing", "Group Morph", TXGroupMorph],
			["Control: Mixing", "Matrix C 8x8", TXMatrixControl8x8],
			["Control: Mixing", "Mix Control 8-1", TXMixControl8to1],
			["Control: Mixing", "X-Fader 2-1 C", TXXFader2to1C],
			["Control: Modulation", "LFO", TXLFOMulti2],
			["Control: Modulation", "LFO Curve", TXLFOCurve],
			["Control: Modulation", "Perlin Noise", TXPerlinNoise],
			["Control: Modulation", "T Noise Ring", TXTNoiseRing],
			["Control: Modulation", "T Random", TXTRandom],
			["Control: Modulation", "T Random Choice", TXTRandomChoice],
			["Control: Modulation", "T Random Walk", TXTRandomWalk],
			["Control: Note Tools", "Note Multiply", TXNoteMultiply],
			["Control: Note Tools", "Note Stacker", TXNoteStacker],
			["Control: Note Tools", "Scale Chord", TXScaleChord],
			["Control: OSC", "OSC Controller", TXOSCController],
			["Control: OSC", "OSC Control 2D ", TXOSCController2D],
			["Control: OSC", "OSC Control Out", TXOSCControlOut],
			["Control: OSC", "OSC Out", TXOSCOut],
			["Control: OSC", "OSC Remote", TXOSCRemote],
			["Control: OSC", "OSC Trigger", TXOSCTrigger],
			["Control: Physical Models", "Ball", TXBall],
			["Control: Physical Models", "Spring", TXSpring],
			["Control: Sequencer", "Action Sequencer", TXActionSeq3],
			["Control: Sequencer", "Mod Sequencer", TXModSequencer],
			["Control: Sequencer", "MultiTrack Seq", TXMultiTrackSeq],
			["Control: Sequencer", "Step Sequencer", TXStepSequencer],
			["Control: Slider", "Action Slider", TXActionSlider],
			["Control: Slider", "Simple Slider", TXSimpleSlider2],
			["Control: SuperCollider", "Animate Code", TXAnimateCode2],
			["Control: SuperCollider", "Code Source C", TXCodeSourceCtrl],
			["Control: SuperCollider", "Pattern Code", TXPatternCode],
			["Control: Switch", "On Off Switch", TXOnOffSwitch],
			["Control: Switch", "Toggle Switch", TXToggleSwitch],
			// ["Control: Quartz", "QC Particles", TXQCParticles2], // not currently working
			// ["Control: Quartz", "Quartz Player", TXQCPlayer4], // not currently working
			// ["Control: TXV System", "TXV System", TXV_System], // not currently working
		];
		dataBank.arrSourceModulesByCategoryWithAlpha = dataBank.arrSourceModulesByCategory.collect({arg item, i;
			["All Source Modules ", item[1], item[2]];
		})
		.sort({ arg a, b; a[1] < b[1] })
		++ dataBank.arrSourceModulesByCategory;

		dataBank.arrAudioInsertModulesByCategory = [
			[">AUDIO INSERTS: ", nil, "audio"],
			[">Channel tools ", nil, "audio"],
			["   Mono to Stereo", TXMonoToStereo, "audio"],
			["   Stereo To Mono", TXStereoToMono2, "audio"],
			["   Stereo Width", TXStereoWidth, "audio"],
			[">Delay ", nil, "audio"],
			["   Delay", TXDelay4, "audio"],
			["   Delay St", TXDelay4St, "audio"],
			["   Live Looper", TXLiveLooper2, "audio"],
			["   MultiTap Delay", TXMultiTapDelay2, "audio"],
			["   Ping Pong", TXPingPong, "audio"],
			["   Ping Pong St", TXPingPongSt, "audio"],
			[">Distortion ", nil, "audio"],
			["   Amp Sim", TXAmpSim, "audio"],
			["   Amp Sim St", TXAmpSimSt, "audio"],
			["   Bit Crusher", TXBitCrusher2, "audio"],
			["   Bit Crusher St", TXBitCrusher2St, "audio"],
			["   Disintegrator", TXDisintegrator, "audio"],
			["   Disintegrator St", TXDisintegratorSt, "audio"],
			["   Distortion", TXDistortion3, "audio"],
			["   Distortion St", TXDistortion3St, "audio"],
			["   Interference", TXInterference, "audio"],
			["   Interference St", TXInterferenceSt, "audio"],
			["   WaveFold", TXWaveFold, "audio"],
			["   WaveFold St", TXWaveFoldSt, "audio"],
			["   Waveshaper", TXWaveshaper, "audio"],
			["   Waveshaper St", TXWaveshaperSt, "audio"],
			["   WaveshaperX", TXWaveshaperX, "audio"],
			["   WaveshaperX St", TXWaveshaperXSt, "audio"],
			["   X Distort", TXXDistort, "audio"],
			["   X Distort St", TXXDistortSt, "audio"],
			[">Dynamics ", nil, "audio"],
			["   Amp Comp", TXAmpComp, "audio"],
			["   Amp Comp St", TXAmpCompSt, "audio"],
			["   Balance", TXBalance, "audio"],
			["   Balance St", TXBalanceSt, "audio"],
			["   Compander", TXCompander3, "audio"],
			["   Compander St", TXCompander3St, "audio"],
			["   Env Follow", TXEnvFollow, "audio"],
			["   Env Follow St", TXEnvFollowSt, "audio"],
			["   Gain", TXGain, "audio"],
			["   Gain St", TXGainSt, "audio"],
			["   Limiter", TXLimiter, "audio"],
			["   Limiter St", TXLimiterSt, "audio"],
			["   Normalizer", TXNormalizer, "audio"],
			["   Normalizer St", TXNormalizerSt, "audio"],
			["   Noise Gate", TXNoiseGate, "audio"],
			["   Noise Gate St", TXNoiseGateSt, "audio"],
			["   SoftKnee Comp", TXSoftKneeComp, "audio"],
			["   SoftKnee Comp St", TXSoftKneeCompSt, "audio"],
			["   Transient Shape", TXTransientShape, "audio"],
			["   Transient Shape St", TXTransientShapeSt, "audio"],
			[">EQ & Filter ", nil, "audio"],
			["   Bracket EQ", TXBracketEQ, "audio"],
			["   Bracket EQ St", TXBracketEQSt, "audio"],
			["   DC Remove", TXDCRemove, "audio"],
			["   DC Remove St", TXDCRemoveSt, "audio"],
			["   Dual Filter", TXDualFilter, "audio"],
			["   Dual Filter St", TXDualFilterSt, "audio"],
			["   EQ 6", TXEQ6, "audio"],
			["   EQ 6 St", TXEQ6St, "audio"],
			["   EQ 6M", TXEQ6M, "audio"],
			["   EQ 6M St", TXEQ6MSt, "audio"],
			["   EQ Graphic", TXEQGraphic, "audio"],
			["   EQ Graphic St", TXEQGraphicSt, "audio"],
			["   EQ Morph", TXEQMorph, "audio"],
			["   EQ Morph St", TXEQMorphSt, "audio"],
			["   EQ Para", TXEQPara, "audio"],
			["   EQ Para St", TXEQParaSt, "audio"],
			["   EQ Shelf", TXEQShelf, "audio"],
			["   EQ Shelf St", TXEQShelfSt, "audio"],
			["   Filter", TXFilterExt2, "audio"],
			["   Filter St", TXFilterExt2St, "audio"],
			[">Gain Envelope ", nil, "audio"],
			["   Gain 16-stage", TXGain16Stage, "audio"],
			["   Gain 16-stage St", TXGain16StageSt, "audio"],
			["   Gain Curve", TXGainCurve, "audio"],
			["   Gain Curve St", TXGainCurveSt, "audio"],
			["   Gain DADSR", TXGainDADSR, "audio"],
			["   Gain DADSR St", TXGainDADSRSt, "audio"],
			["   Gain DADSSR", TXGainDADSSR, "audio"],
			["   Gain DADSSR St", TXGainDADSSRSt, "audio"],
			[">Granulation ", nil, "audio"],
			["   Live Grain", TXLiveGrain, "audio"],
			[">Modulation ", nil, "audio"],
			["   Chorus", TXChorus, "audio"],
			["   Chorus St", TXChorusSt, "audio"],
			["   Flanger", TXFlanger3, "audio"],
			["   Flanger St", TXFlanger3St, "audio"],
			["   Notch Phaser", TXNotchPhaser, "audio"],
			["   Notch Phaser St", TXNotchPhaserSt, "audio"],
			["   Phaser", TXPhaser, "audio"],
			["   Phaser St", TXPhaserSt, "audio"],
			["   Ring Modulator", TXRingMod2, "audio"],
			[">Pitch Shift ", nil, "audio"],
			["   Pitch Shifter", TXPitchShifter, "audio"],
			["   Pitch Shifter St", TXPitchShifterSt, "audio"],
			[">Recording ", nil, "audio"],
			["   File Recorder", TXFileRecorder2, "audio"],
			["   File Recorder St", TXFileRecorder2St, "audio"],
			[">Resonator ", nil, "audio"],
			["   Bowed Res", TXBowedRes, "audio"],
			["   Formlets", TXFormlets, "audio"],
			["   Klank", TXKlank, "audio"],
			["   Membrane", TXMembrane, "audio"],
			["   Piano Res St", TXPianoResSt, "audio"],
			["   Strings", TXStrings, "audio"],
			["   Tubes", TXTubes, "audio"],
			[">Reverb ", nil, "audio"],
			["   Convolution ", TXConvolution, "audio"],
			["   Convolution St ", TXConvolutionSt, "audio"],
			["   Greyhole", TXGreyhole, "audio"],
			["   Greyhole St", TXGreyholeSt, "audio"],
			["   Infinity", TXInfinity, "audio"],
			["   Infinity St", TXInfinitySt, "audio"],
			["   JPverb", TXJPverb, "audio"],
			["   JPverb St", TXJPverbSt, "audio"],
			["   Reverb", TXReverb2, "audio"],
			["   Reverb St", TXReverbSt2, "audio"],
			["   ReverbA", TXReverbA, "audio"],
			["   ReverbF", TXReverbF, "audio"],
			["   ReverbF St", TXReverbFSt, "audio"],
			["   ReverbG", TXReverbG, "audio"],
			[">Spectral ", nil, "audio"],
			["   Spectral Delay", TXSpectralDelay, "audio"],
			["   Spectral FX", TXSpectralFX, "audio"],
			["   Vocoder", TXVocoder2, "audio"],
			["   Vocoder FX", TXVocoderFX2, "audio"],
			[">SuperCollider ", nil, "audio"],
			["   Code Insert A", TXCodeInsertAu, "audio"],
			["   Code Insert A St", TXCodeInsertAuSt, "audio"],
			[">Synthesis ", nil, "audio"],
			["   Harmoniser", TXHarmoniser, "audio"],
			["   Sub Harm", TXSubHarm, "audio"],
			["   Sub Harm St", TXSubHarmSt, "audio"],
		];

		dataBank.arrControlInsertModulesByCategory = [
			[">CONTROL INSERTS ", nil, "control"],
			[">Analysis ", nil, "control"],
			["   Slope", TXSlope, "control"],
			["   Changed Trig", TXChangedTrig, "control"],
			[">Delay ", nil, "control"],
			["   Control Delay", TXControlDelay4, "control"],
			[">Modify ", nil, "control"],
			["   Quantise", TXQuantise, "control"],
			["   Sample and Hold", TXSampleHold, "control"],
			["   Scale Quantise", TXScaleQuantise, "control"],
			["   Smooth", TXSmooth2, "control"],
			["   Trig to Gate", TXTrig2Gate, "control"],
			["   Warp ", TXWarp2, "control"],
			["   Warp Morph ", TXWarpMorph, "control"],
			[">SuperCollider ", nil, "control"],
			["   Code Insert C", TXCodeInsertCtrl, "control"],
		];

		dataBank.arrInsertModulesByCategoryWithAlpha = [[">All Insert Modules ", nil, "all" ]]
		++ (dataBank.arrAudioInsertModulesByCategory.keep(1-dataBank.arrAudioInsertModulesByCategory.size)
			++ dataBank.arrControlInsertModulesByCategory.keep(1-dataBank.arrControlInsertModulesByCategory.size)
		).select({arg item, i; item[0][0].asString != ">"})
		.sort({ arg a, b; a[0] < b[0] })
		++ dataBank.arrAudioInsertModulesByCategory.keep(1-dataBank.arrAudioInsertModulesByCategory.size)
		++ dataBank.arrControlInsertModulesByCategory.keep(1-dataBank.arrControlInsertModulesByCategory.size);
	}

	*instSortingName {
		^instName;
	}

	////////////////////////////////////////////////////////////////////////////////////
	// start the system

	*start { arg argStandAlone = 0, argFileNameString, showAudioOptions = true;
		var holdString;
		var classError = "";

		// if open already, window to front and return
		if (TXSystem1GUI.w.notNil and: {TXSystem1GUI.w.isClosed != true}, {
			TXSystem1GUI.w.front;
			^0;
		});

		// init
		dataBank.windowVisibleOrigin = Point.new(0,0);
		//this.closeAllHIDDevices; // testing with/out

		if (argStandAlone == 1, {
			txStandAlone = 1; // for running TX system as a standalone
			// removed for now
			//		showAudioOptions = false; // force to false if standalone
		});

		//check for relevant plugins/quarks
		if ('VOSIM'.asClass.isNil, {
			classError = classError ++ "Error - SC3 Plugins not installed.  ";
		});
		if ('SimpleMIDIFile'.asClass.isNil, {
			classError = classError ++ "Error - wslib quark not installed.  ";
		});
		if (classError != "", {
			TXInfoScreen.new("Error - unable to start TX Modular System due to the following:  " ++ classError);
			^0;
		});

		//  set variables:
		dataBank.loadingDataFlag = false;
		dataBank.arrModulesForRebuilding = [].asSet;
		dataBank.confirmDeletions = true;
		dataBank.windowAlpha = 1;
		dataBank.imageFileName = nil;
		dataBank.recentFileNames = [];
		dataBank.displayModeIndex = 1;
		dataBank.holdImage = nil;
		dataBank.volume = 0;
		dataBank.ipAddress = "__________ ";
		dataBank.windowColour = TXColor.sysMainWindow;
		this.updateIPAddress;
		dataBank.showAudioOptions = showAudioOptions;
		this.removeOldSynthDefs;
		this.loadSystemSettings;

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
			showWindow = 'Run Interface';
			showFrontScreen = true;
		},{
			showWindow = 'Modules & Channels';
			showFrontScreen = false;
		});

		this.clearHistory;

		// bypass globalMouseDown test for now - causes crashes
		//	globalMouseDown = false;

		// create Load Queue
		holdLoadQueue = TXLoadQueue.new;

		// adjust for osx
		// if (thisProcess.platform.name != \osx, {
		// 	arrAllPossModules.remove(TXQCParticles2);
		// 	arrAllPossModules.remove(TXQCPlayer4);
		// 	dataBank.arrSourceModulesByCategory.remove(["Control: Visual", "QC Particles", TXQCParticles2]);
		// 	dataBank.arrSourceModulesByCategory.remove(["Control: Visual", "Quartz Player", TXQCPlayer4]);
		// });

		// create arrays of all possible sequencer modules
		arrAllPossCurSeqModules = [
			TXActionSeq3,
			TXStepSequencer,
			TXMultiTrackSeq,
			// TXModSequencer, not relevent here
		];
		autoOpen = false;
		dataBank.closingDown = false;

		// initialise
		dataBank.notes0 = " ";
		dataBank.notes1 = " ";
		dataBank.notes2 = " ";
		dataBank.notes3 = " ";
		dataBank.notes4 = " ";
		dataBank.notes5 = " ";
		dataBank.notes6 = " ";
		dataBank.notes7 = " ";
		arrSnapshots  = Array.newClear(100);
		dataBank.snapshotNo = 0;
		dataBank.snapshotName = "";
		arrModuleClipboards = Dictionary.new;

		// REMOVED:
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

		dataBank.audioSetupQuit = false;
		this.startMain(argFileNameString);

	}	// end of *start method

	*startMain{ arg argFileNameString;
		var	holdFileName, holdData, holdFileNameString, holdString;
		var holdInfoScreen, holdStartFunction;

		// Create a new instance of ServerOptions
		dataBank.holdServerOptions = ServerOptions.new;
		dataBank.holdServerOptions.numInputBusChannels = 16;
		dataBank.holdServerOptions.numOutputBusChannels = 16;
		dataBank.holdServerOptions.numAudioBusChannels = 128 * 4;
		dataBank.holdServerOptions.numControlBusChannels = 4096 * 4;
		dataBank.holdServerOptions.memSize = 65536 * 4;
		dataBank.holdServerOptions.numWireBufs = 64* 16;
		//dataBank.holdServerOptions.zeroConf = true;
		dataBank.holdServerOptions.zeroConf = false;
		dataBank.holdServerOptions.verbosity = -1;
		dataBank.holdServerOptions.device = dataBank.audioDevice;
		dataBank.holdServerOptions.hardwareBufferSize = dataBank.bufferSize;

		// TESTING XXX  - if using iMac internal audio card, defSampleRate often causes error:
		//     ERROR: server failed to start
		// CHANGED FOR NOW
		// dataBank.holdServerOptions.sampleRate = dataBank.sampleRate ? defSampleRate;
		dataBank.holdServerOptions.sampleRate = dataBank.sampleRate;

		// TESTING XXX  - REMOVED FOR NOW
		// if (txStandAlone == 1, {
		// 	dataBank.holdServerOptions.ugenPluginsPath =  [
		// 		Platform.resourceDir +/+ "plugins",
		// 		Platform.resourceDir +/+ "SCClassLibrary",
		// 	];
		// });

		holdStartFunction = {
			// Show starting info screen
			server.options = dataBank.holdServerOptions;
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
							//["commandAction", "Refresh screen", {{this.flagGuiUpd;}.defer(0.1)}],
							["commandAction", "Refresh screen", {this.showView;}],
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
						dataBank.holdNextModuleID = 100;
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
					dataBank.screenUpdRoutine = Routine { arg inval;
						loop {
							// run all the screen update functions
							this.runScreenUpdFuncs;
							dataBank.screenUpdPauseTime.wait;	// pause secs
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
			if (dataBank.audioSetupQuit == false, {
				TXAudioOptionsScreen.makeWindow (this, dataBank.holdServerOptions, holdStartFunction);
			});
		});
	}	// end of *startMain method

	////////////////////////////////////////////////////////////////////////////////////

	*closeSystem {
		// if standalone system then quit completely
		if (txStandAlone == 1, {
			this.quitStandalone;
		},{
			// set variables
			dataBank.closingDown = true;
			dataBank.holdFileName = "";
			// stop routines
			//this.closeAllHIDDevices; // testing with/out
			dataBank.screenUpdRoutine.stop;
			{TXHelpScreen.close;}.defer;
			if (FreqScope.scopeOpen == true and: dataBank.holdFreqScope.notNil, {
				dataBank.holdFreqScope.window.close;
			});
			TXClipboardWindow.closeWindow;
			TXCurveBuilder.closeWindow;
			TXColor.closePickerWindow;
			// empty system
			this.emptySystem;
			//	NOTE - removed for now, can cause crashes
			//		// stop mouse synth
			//		mouseButtonSynth.free;
			//		mouseButtonResponder.remove;
			//	end
			//	server.freeAll;
			{server.quit}.defer(1);
		});
	}

	*closeAllHIDDevices {
		// var openDevices = HID.openDevices;
		// if (openDevices.size > 0, {
		// 	("TX Closing " ++ openDevices.size ++ " HID devices...").postln;
		// 	openDevices.do {arg item, i; item.close;};
		// });
		"TX: Closing ALL HID Devices".postln;
		HID.closeAll;
	}

	*quitStandalone {
		//0.exit;
		//"killall SuperCollider".unixCmd;
		("killall " ++ dataBank.standaloneName).unixCmd;
	}

	*emptySystem {
		TXMeterBridge.close;
		TXOscilloscope.closeWindow;
		TXFreqScope.closeWindow;
		// stop all sequencers in system
		this.stopAllSyncModules;
		// clear system clock
		SystemClock.clear;
		// deactivate any midi and keydown functions
		TXFrontScreen.midiDeActivate;
		TXFrontScreen.keyDownDeActivate;
		// delete all channels in arrChannels in TXChannelRouting
		TXChannelRouting.deleteAllChannels;
		// delete all current modules in system twice
		arrSystemModules.do({ arg item, i; item.deleteModule;});
		arrSystemModules.do({ arg item, i; item.deleteModule;});
		// run inits
		TXBankBuilder2.initClass;
		TXWidget.initClass;
		TXFrontScreen.initClass;
		//	TXSeqGui.initClass;//	TXModGui.initClass;	dataBank.snapshotNo = 0;
		dataBank.snapshotName = "";
		dataBank.savedSystemRevision = 0;
		this.clearHistory;
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
				dataBank.audioInDevice, dataBank.audioOutDevice, dataBank.recentFileNames,
				dataBank.screenUpdateRate, dataBank.screenUpdPauseTime,
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
		holdFile = PathName.new(Platform.userAppSupportDir +/+ "TXModular" +/+ "TXModSettings.tx");

		// if TXModular directory doesn't exist, create it.
		if (holdPath.isFolder.not, {
			// OLD CODE
			// FIXME: this won't work on Windows
			// ("mkdir" + holdPath.fullPath).unixCmd;
			// NEW CODE
			holdPath.fullPath.makeDir;
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
				}, {
					dataBank.audioDevice = nil;
					dataBank.bufferSize = nil;
					dataBank.sampleRate = nil;
					dataBank.audioInDevice = nil;
					dataBank.audioOutDevice = nil;
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
				if (holdFileData[1][13].notNil, {
					dataBank.recentFileNames = holdFileData[1][13];
				});
				if (holdFileData[1][14].notNil, {
					dataBank.screenUpdateRate = holdFileData[1][14];
				});
				if (holdFileData[1][15].notNil, {
					dataBank.screenUpdPauseTime = holdFileData[1][15];
				});
				validData = true;
			});
		});
		if (validData != true,  {
			// if file TXMODSettings.tx  doesn't exist, create it.
			this.saveSystemSettings;
		});
	}

	*removeOldSynthDefs{
		var dir = PathName.new(SynthDef.synthDefDir);
		dir.filesDo({|afile|
			if (afile.fileName.keep(3) == "TX_", {
				File.delete(afile.fullPath);
			});
		});
	}

	///  SYNC START AND STOP SYSTEM /////////////////////////////////////

	*syncStart{
		this.syncStartSequencers;
		this.syncStartPatternCode;
		this.syncStartRecorders;
		this.syncStartPlayers;
	}

	*syncStop{
		this.syncStopSequencers;
		this.syncStopPatternCode;
		this.syncStopRecorders;
		this.syncStopPlayers;
	}

	*stopAllSyncModules {
		this.stopAllSequencers;
		this.stopAllPatternCode;
		this.stopAllRecorders;
		this.stopAllPlayers;
	}
	/////// called by above 3: //////

	*syncStartSequencers{
		this.arrAllPossCurSeqModules.do ({ arg item, i;
			item.syncStartAllSequencers;
		});
	}
	*syncStopSequencers{
		this.arrAllPossCurSeqModules.do ({ arg item, i;
			try{item.syncStopAllSequencers;};
		});
	}
	*stopAllSequencers{
		this.arrAllPossCurSeqModules.do ({ arg item, i;
			item.stopAllSequencers;
		});
	}
	*syncStartPatternCode{
		TXPatternCode.syncStartAll;
	}
	*syncStopPatternCode{
		TXPatternCode.syncStopAll;
	}
	*stopAllPatternCode{
		TXPatternCode.stopAll;
	}
	*syncStartRecorders{
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
		TXFilePlayer6.syncStartAllPlayers;
		TXFilePlayer6St.syncStartAllPlayers;
	}
	*syncStopPlayers{
		TXFilePlayer6.syncStopAllPlayers;
		TXFilePlayer6St.syncStopAllPlayers;
	}
	*stopAllPlayers {
		TXFilePlayer6.stopAllPlayers;
		TXFilePlayer6St.stopAllPlayers;
	}
	////////////////////////////////////////////////////////////////////////////////////

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
		if (argString == "Snapshot", {^dataBank.snapshotNo});
		^0;
	}
	*setSynthValue {
		// Dummy method
		^0;
	}

	*updateIPAddress {
		// removed for now:
		//
		// var holdFlag = NetAddr.broadcastFlag;
		// var holdAddress;
		//
		// NetAddr.broadcastFlag = true;
		// OSCresponder(nil, '/getMyIP', { |t,r,msg,addr|
		// 	dataBank.ipAddress = addr.ip;
		// 	NetAddr.broadcastFlag = holdFlag;
		// 	this.showView;
		// }).add;
		// holdAddress = NetAddr("255.255.255.255", NetAddr.langPort);
		// if (holdAddress.isConnected, {
		// 	holdAddress.sendMsg('/getMyIP');
		// });
	}

	*midiConnect {
		MIDIClient.init;
		MIDIIn.connectAll;
	}
	////////////////////////////////////////////////////////////////////////////////////


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

	*arrTXEvents { arg matchModuleID;
		// returns an array of events that can be used with Pbind patterns
		// only objects of type \number and \checkbox are included that expect numbers
		var holdArrModules, holdArrActionSpecs, holdArrAllEvents;
		holdArrAllEvents = (); // this is in itself an Event
		holdArrModules = this.arrWidgetActionModules;
		holdArrModules.do({arg argModule, i;
			var holdArrNumericalActionSpecs, holdArrModuleEvents;
			// filter with matchModuleID if passed
			if (matchModuleID.isNil or: (argModule.moduleID == matchModuleID), {
				holdArrModuleEvents = [];
				holdArrNumericalActionSpecs = argModule.arrActionSpecs.select({arg action, i;
					(action.actionName != "...")
					and: (action.actionName != "---/")
					and: ((action.guiObjectType == \number) or: (action.guiObjectType == \checkbox));
				});
				holdArrNumericalActionSpecs.do({arg argActionSpec, i;
					var holdValCount, holdEvent, arrValNames;
					if (argActionSpec.guiObjectType == \checkbox, {
						holdValCount = 1;
						},{
							holdValCount = argActionSpec.arrControlSpecFuncs.size;
					});
					holdEvent =  ( // new event
						moduleName: argModule.instName,
						actionName: argActionSpec.actionName,
						val1: 0,
						val2: 0,
						val3: 0,
						val4: 0,
						dur: 1,
						valCount: holdValCount,
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
				});
			});
			// end of holdArrNumericalActionSpecs.do
			holdArrAllEvents[argModule.moduleID] = holdArrModuleEvents;
		});
		// end of holdArrModules.do
		^holdArrAllEvents;
	}

	*displayAllTXEvents {
		var a, b, c, w;
		a = this.arrTXEvents;
		c = " \n" ++
		"Events that work with Pbind patterns in the current TX Modular system" ++ "\n" ++
		"\n" ++
		"Format:  arrTXEvents [Module ID] [Action ID] Module name - Action name (no. numeric arguments)" ++ "\n" ++
		"\n";
		b = [];
		a.keysDo({arg argModuleID, argModNo;
			var arrStrings;
			arrStrings = [];
			a[argModuleID].do({arg item, i;
				var holdString, gapString;
				// holdString = "[" ++ argModuleID ++ "]["++ i.asString ++ "]";
				// holdString = holdString.keep(14) + item.moduleName ++ " -" + item.actionName;
				holdString = "arrTXEvents[" ++ argModuleID ++ "]["++ i.asString ++ "]"
				+ item.moduleName + "-" + item.actionName
				+ "(" ++ item.valCount + "args)";
				arrStrings = arrStrings.add(holdString);
			});
			b = b.add([this.getModuleFromID(argModuleID).instSortingName, arrStrings]);
		});
		b = b.sort({arg a, b; a[0] < b[0]});
		b.do({ arg group;
			c = c ++ "-------------------------------------------------------------------------------\n";
			group[1].do({ arg item, i;
				c = c ++ item ++ "\n";
			});
		});
		c = c ++ "-------------------------------------------------------------------------------\n";

		w = Window("TX Events", Rect(20, 800, 600, 600));
		w.front;
		w.view.decorator = FlowLayout(w.view.bounds);
		w.view.decorator.shift(10,10);
		TextView(w, 580 @ 580)
		.resize_(5)
		.string_(c)
		.background_(TXColor.white);
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
		arrNotes = [dataBank.notes0, dataBank.notes1, dataBank.notes2, dataBank.notes3, dataBank.notes4,
			dataBank.notes5, dataBank.notes6, dataBank.notes7];
		// collect front screen data
		arrFrontScreenData = TXFrontScreen.saveData;
		// collect module layout data
		arrModuleLayoutData = TXSignalFlow.saveData;
		// build output
		arrData = ["TXSystemSaveData", this.class.asString, dataBank.holdNextModuleID, arrAllModulesData,
			channelRoutingData, arrBusData, this.arrSampleBanks, this.arrLoopBanks, arrNotes,
			arrFrontScreenData, arrSnapshots, arrModuleLayoutData, dataBank.systemRevision,
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
		arrBusData, arrNotes, arrFrontScreenData, arrModuleLayoutData, arrAllClassNames;

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

		// temporarily store and turn off system.autoOpen while building system
		holdAutoOpen = autoOpen;
		autoOpen = false;

		// flag loading data
		dataBank.loadingDataFlag = true;

		// assign variables
		dataBank.savedSystemRevision = arrData.at(12) ? 0;
		dataBank.holdNextModuleID = arrData.at(2).copy;
		arrAllModulesData = arrData.at(3).deepCopy;
		// confirm all classes are valid before continuing
		arrAllClassNames = Class.allClasses.collect({arg item; item.name});
		arrAllModulesData.do({ arg item, i;
			var holdClassName;
			holdClassName = item.at(1);
			// show error if class not found
			if (arrAllClassNames.indexOf(holdClassName.asSymbol).isNil, {
				TXInfoScreen.new("Error: Cannot load this file in the current TX Modular, contains out of date module class: "
					++ holdClassName);
				^0;
			});
		});
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
		// 13-background imageFileName, 14-background image displayModeIndex, 15-windowAlpha,
		// 16-windowColour.red, 17-windowColour.green, 18-windowColour.blue, 19-windowColour.alpha
		if (arrData.at(15).notNil, {
			dataBank.windowAlpha = arrData.at(15);
		});
		// removed for now:
		// if (arrData.at(16).notNil, {
		// 	dataBank.windowColour = Color(arrData.at(16), arrData.at(17), arrData.at(18), arrData.at(19));
		// 	TXSystem1GUI.w.view.background = dataBank.windowColour;
		// 	},{
		// 	dataBank.windowColour = TXColor.sysMainWindow;
		// 	TXSystem1GUI.w.view.background = dataBank.windowColour;
		// });

		TXSystem1GUI.setWindowImage;

		// set notes
		dataBank.notes0 = arrNotes.at(0);
		dataBank.notes1 = arrNotes.at(1);
		dataBank.notes2 = arrNotes.at(2);
		dataBank.notes3 = arrNotes.at(3);
		dataBank.notes4 = arrNotes.at(4);
		dataBank.notes5 = arrNotes.at(5);
		dataBank.notes6 = arrNotes.at(6);
		dataBank.notes7 = arrNotes.at(7);
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
			arrAllModulesData.sort({ arg a, b; a[2] < b[2]}).do({ arg item, i;
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
				// flag if legacy module
				if (arrAllPossModules.indexOf(holdModuleClass).isNil && (holdModuleClass.asString.keep(4) != "TXV_"), {
					//("Info: Opening " ++ holdModuleClass.name.asString ++ " - this is an older TX module.").postln;
					newModule.legacyModule = true;
				});

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
			// restore outputs on relevant modules
			arrAllPossModules.do ({ arg item, i;
				if (item.respondsTo('restoreAllOutputs'), {
					item.restoreAllOutputs;
				});
			});
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
			if (totalWidgets > 1, {showFrontScreen = true; showWindow = 'Run Interface';});

			// add condition to load queue
			holdCondition = holdLoadQueue.addCondition;
			// pause
			holdCondition.wait;

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
			TXChannelRouting.setSelectionToEdit;
			// update view
			this.showView;
		};
		^1; // return 1 to show ok
	}  // end of method loadData

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
		dataBank.snapshotName = argSnapshotName ? "";
		if (dataBank.snapshotName == "", {dataBank.snapshotName = "Snapshot " ++ dataBank.snapshotNo.asString;});
		holdSnapshot = [dataBank.snapshotName, this.saveSnapshotData].deepCopy;
		arrSnapshots.put(dataBank.snapshotNo, holdSnapshot);
	}

	*overwriteCurrentSnapshot {
		var holdSnapshot, holdSnapshotName;
		holdSnapshotName = this.getSnapshotName(dataBank.snapshotNo);
		holdSnapshot = [holdSnapshotName, this.saveSnapshotData].deepCopy;
		arrSnapshots.put(dataBank.snapshotNo, holdSnapshot);
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
		arrSnapshots.put(dataBank.snapshotNo, nil);
		dataBank.snapshotName = "";
	}


	*loadSnapshot { arg argSnapshotNo;
		var holdSnapshot;
		holdSnapshot = arrSnapshots.at(argSnapshotNo.asInteger);
		dataBank.snapshotNo = argSnapshotNo;
		if (holdSnapshot.isNil, {
			dataBank.snapshotName = "";
		}, {
			dataBank.snapshotName = holdSnapshot.at(0);
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
		// testing xxx - post message
		// ("Adding Module: " ++ newModule.instName).postln;
		^newModule;
	}

	*nextModuleID {
		var outModuleID;
		outModuleID = dataBank.holdNextModuleID;
		dataBank.holdNextModuleID = dataBank.holdNextModuleID + 1;
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
		if (dataBank.closingDown == false, {
			// check TXChannelRouting for deletion effects
			TXChannelRouting.checkDeletions;
			// check dataBank.historyEvents
			dataBank.historyEvents = dataBank.historyEvents.reject({arg item, i;
				(item.showWindow == 'Modules & Channels')
				and: {item.displayModule.notNil}
				and: {item.displayModule.deletedStatus == true};
			});
			dataBank.historyIndex = (dataBank.historyEvents.size - 1);
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
		holdBusses.do({arg item, i; item.posX = 0; item.posY = (i+1) * 50;});
	}

	*setScreenUpdateRate { arg rate;
		dataBank.screenUpdateRate = rate;
		dataBank.screenUpdPauseTime = rate.reciprocal;
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

	*makeGui {
		TXSystem1GUI.makeWindow;
		this.showView;
	}

	*showView {
		dataBank.screenRebuild = true;
	}

	*windowToFront {
		{TXSystem1GUI.w.front}.defer;
	}

	*addScreenUpdFunc { arg argArray, argScreenUpdFunc;
		// passed argument array and function to be valued
		dataBank.arrScreenUpdFuncs = dataBank.arrScreenUpdFuncs.add([argArray, argScreenUpdFunc]);
	}

	*runScreenUpdFuncs {
		// bypass globalMouseDown test for now
		//	// only run code if mouse is not down to stop crashes
		//	if (globalMouseDown == false, {
		if ((Main.elapsedTime - dataBank.holdBootSeconds) > 0.5, {
			if (dataBank.screenRebuild == true, {
				{ // defer function
					TXSystem1GUI.buildGUI;
					dataBank.holdBootSeconds = Main.elapsedTime;
				}.defer;
			}, {
				// don't run if Design Interface shown
				if (showWindow != 'Design Interface' and: {dataBank.screenChanged == true}, {
					{ // defer function
						dataBank.arrScreenUpdFuncs.do({arg item, i;
							var arrArgs = item.at(0);
							var holdFunc = item.at(1);
							var view = arrArgs[0];
							var runFunc = true;
							if (view.respondsTo('hasFocus') and: {view.hasFocus == true}, {
								runFunc = false;
							});
							if (runFunc, {
								holdFunc.value(arrArgs);
							});
						});
					}.defer;
				});
			});
			dataBank.screenRebuild = false;
			dataBank.screenChanged = false;
		});
		//	});
	}

	*clearScreenUpdFuncs {
		dataBank.arrScreenUpdFuncs = [];
	}

	*flagGuiUpd {
		dataBank.screenChanged = true;
	}

	*flagGuiIfModDisplay {	arg argModule;
		// if argModule is currently being displayed on screen, then rebuild view
		if (((showFrontScreen == true ) /* and: {TXFrontScreen.classData.allowFrontScreenUpdates == true} */ )
			|| ((showWindow == 'Modules & Channels')
				and: ((TXChannelRouting.displayModule == argModule) || (argModule.class == TXChannel))),
			{
				this.flagGuiUpd;
		});
	}

	*showViewIfModDisplay {	arg argModule;
		// if argModule is currently being displayed on screen, then rebuild view
		if ((showWindow == 'Modules & Channels') and: (TXChannelRouting.displayModule == argModule),
			{this.showView});
	}

	*showViewIfRunInterface {
		// if interface is currently being displayed on screen, then rebuild view
		if ((showWindow == 'Run Interface'),
			{this.showView});
	}

	*isModDisplay {	arg argModule;
		var returnVal = false;
		// if argModule is currently being displayed on screen, then return true
		if ((showWindow == 'Modules & Channels') and: (TXChannelRouting.displayModule == argModule),
			{returnVal = true;});
		^returnVal;
	}

	*showModulesAndChannels {
		showWindow = 'Modules & Channels';
		this.showView;
	}

	///////////////////////////////////////////////////////////////////////////////////

	*loadFileName { arg newPath;
		var newData, loadResult;
		if (File.exists(newPath), {
			this.storeRecentFileName(newPath);
			// post message
			("TX Opening File: " ++ newPath).postln;
			newData = thisProcess.interpreter.executeFile(newPath);
			dataBank.holdFileName = "    File name: " ++ newPath;
			dataBank.loadingDataFlag = true;
			this.showView;
			loadResult = this.loadData(newData);
			// check error
			if (loadResult != 1, {
				dataBank.holdFileName = "";
				dataBank.loadingDataFlag = false;
				this.showView;
			});
		}, {
			TXInfoScreen.new("Error - cannot open, file not found: " ++ newPath);
		});
	}

	*storeRecentFileName { arg newPath;
		var holdIndex, tempNames;
		holdIndex = dataBank.recentFileNames.indexOfEqual(newPath);
		if (holdIndex.notNil, {
			tempNames = dataBank.recentFileNames.deepCopy;
			tempNames.removeAt(holdIndex);
			dataBank.recentFileNames = tempNames;
		});
		dataBank.recentFileNames = dataBank.recentFileNames.addFirst(newPath);
		dataBank.recentFileNames = dataBank.recentFileNames.keep(200);
		this.saveSystemSettings; // save settings with new recentFileNames
	}

	*clearRecentFileNames {
		dataBank.recentFileNames = [];
		this.saveSystemSettings; // save settings with new recentFileNames
	}

	///////////////////////////////////////////////////////////////////////////////////

	*printListOfModules {
		("======== List of Modules in the System with id ===========: ").postln;
		arrSystemModules.do({arg item, i;
			("Name & ID: " ++ item.instName ++ ",  " ++ item.moduleID).postln;
		});
	}

	// history /////////////////////////////////////////////////////////////////////////////////

	*shiftHistory {arg shiftVal;
		var newIndex, holdEvent;
		newIndex = (dataBank.historyIndex + shiftVal).max(0).min((dataBank.historyEvents.size - 1).max(0));
		if (dataBank.historyIndex != newIndex, {
			dataBank.historyIndex = newIndex;
			holdEvent = dataBank.historyEvents[dataBank.historyIndex];
			showWindow = holdEvent.showWindow;
			showFrontScreen = holdEvent.showFrontScreen;
			if (showWindow == 'Modules & Channels', {
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
		dataBank.historyEvents = [];
		dataBank.historyIndex = 0;
		this.addHistoryEvent;
	}

	*addHistoryEvent {
		var holdEvent;
		holdEvent = ();
		holdEvent.showWindow = showWindow;
		holdEvent.showFrontScreen = showFrontScreen;
		holdEvent.displayModule = TXChannelRouting.displayModule;
		holdEvent.showModuleBox = TXChannelRouting.showModuleBox;
		holdEvent.layerNo = TXFrontScreen.classData.layerNo;
		if (dataBank.historyEvents[dataBank.historyIndex] != holdEvent, {
			// remove any history after current index before adding
			dataBank.historyEvents = dataBank.historyEvents.keep(dataBank.historyIndex + 1);
			dataBank.historyEvents = dataBank.historyEvents.add(holdEvent);
		});
		dataBank.historyEvents = dataBank.historyEvents.keep(-10);
		dataBank.historyIndex = dataBank.historyEvents.size - 1;
	}

}
