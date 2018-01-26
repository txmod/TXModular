// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFilePlayer6St : TXModuleBase {	// Disk File Player - stereo

	classvar <>classData;

	var	<>sampleFileNameView;
	var	<>sampleFileName = "";
	var sampleNumChannels = 0;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "File Player St";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		// coding note re: arrCtlSCInBusSpecs
		// modulation is set on by default so modulation works straight away in current synth
		classData.arrCtlSCInBusSpecs = [
			["Level", 1, "modLevel", 1],
			["Play speed", 1, "modPlayRate", 1]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [ ["bufnumSound", 32768, 2] ];
	} // end of method initClass

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*syncStartAllPlayers {
		classData.arrInstances.do({ arg item, i;
			item.syncStartPlayer;
		});
	}

	*syncStopAllPlayers {
		classData.arrInstances.do({ arg item, i;
			item.syncStopPlayer;
		});
	}

	*stopAllPlayers {
		classData.arrInstances.do({ arg item, i;
			item.stopPlayer;
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		midiListen = 0;	// don't respond to midi input
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, \ir],
			["velocity", 0, \ir],
			["bufnumSound", 0, \ir],
			["level", 0.5, defLagTime],
			["attack", 0.01, defLagTime],
			["release", 0.01, defLagTime],
			["syncStart", 0, \ir],
			["syncStop", 0, \ir],
			["loop", 0, 0],
			["playRate", 0.090909090909091, defLagTime],
			["playRateMin", 0.1, defLagTime],
			["playRateMax", 10.0, defLagTime],
			["modLevel", 0, defLagTime],
			["modPlayRate", 0, defLagTime],
		];
		synthDefFunc = {
			arg out, gate, note, velocity, bufnumSound, level, attack, release, syncStart, syncStop,
			loop, playRate, playRateMin, playRateMax, modLevel=0, modPlayRate=0;
			var outEnv, outSound, playRateCombined, outLevel;

			outEnv = EnvGen.kr(
				Env.new([0,1,1,0],[attack, 1, release],'sine', 2),
				gate,
				doneAction: 2
			);
			playRateCombined = playRateMin + ( (playRateMax - playRateMin)
				* (playRate + modPlayRate).max(0).min(1));
			outLevel = (level + modLevel).max(0).min(1);
			outSound = VDiskIn.ar(2, bufnumSound, playRateCombined, loop) * outLevel;
			Out.ar(out, outEnv * outSound);
		};
		guiSpecArray = [
			["ActionButtonBig", "Open new file", {this.openNewFile}],
			["NextLine"],
			["TXStaticText", "File name",
				{this.sampleFileName.keep(-50)}, {arg view; sampleFileNameView = view.textView}],
			["SpacerLine", 4],
			["EZslider", "Fade-in time", ControlSpec(0, 20), "attack"],
			["EZslider", "Fade-out time", ControlSpec(0, 20), "release"],
			["SpacerLine", 4],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SpacerLine", 4],
			["TXCheckBox", "Loop file", "loop"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Play speed", ControlSpec(0.1, 10),
				"playRate", "playRateMin", "playRateMax"],
			["SpacerLine", 4],
			["SeqSyncStartCheckBox"],
			["SeqSyncStopCheckBox"],
			["SpacerLine", 4],
			["ActionButtonBig", "Rewind", {
				moduleNode.release(0);
				this.reCueSample;
			}],
			["Spacer", 3],
			["ActionButtonBig", "Play", {moduleNode.release(0); this.createSynthNote(60, 127, 0); }],
			["Spacer", 3],
			["ActionButtonBig", "Play from start",
				{moduleNode.release(0); this.reCueSample; this.createSynthNote(60, 127, 0); }, 90],
			["Spacer", 3],
			["ActionButtonBig", "Fade out", {moduleNode.release; }],
			["Spacer", 3],
			["ActionButtonBig", "Stop", {moduleNode.release(0); }],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Open new file", {this.openNewFile}],
			["NextLine"],
			["TXStaticText", "File name", {this.sampleFileName},
				{arg view; sampleFileNameView = view}],
			["commandAction", "Play", {
				moduleNode.release(0);
				this.createSynthNote(60, 127, 0); }],
			["commandAction", "Play from start", {
				moduleNode.release(0);
				this.reCueSample;
				this.createSynthNote(60, 127, 0);
			}],
			["commandAction", "Stop with fade out", {moduleNode.release; }],
			["commandAction", "Stop immediately", {moduleNode.release(0); }],
			["commandAction", "Rewind", {
				moduleNode.release(0);
				this.reCueSample;
			}],
			["SeqSyncStartCheckBox"],
			["SeqSyncStopCheckBox"],
			["EZslider", "Fade-in time", ControlSpec(0, 20), "attack"],
			["EZslider", "Fade-out time", ControlSpec(0, 20), "release"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["TXCheckBox", "Loop file", "loop"],
			["TXMinMaxSliderSplit", "Play speed", ControlSpec(0.1, 10),
				"playRate", "playRateMin", "playRateMax"],
		]);
		// use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
	} // end of method init

	extraSaveData { // override default method
		^[sampleFileName];
	}

	loadExtraData {arg argData;  // override default method
		var holdPath;
		// Convert path
		holdPath = TXPath.convert(argData.at(0));
		if (sampleFileName != holdPath, {
			sampleFileName = holdPath;
			Routine.run {
				var holdModCondition;
				// add condition to load queue
				holdModCondition = system.holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				system.server.sync;
				0.1.wait;
				// cue sample
				this.cueSample(sampleFileName);
				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdModCondition);
			};
		});
	}

	allNotesOff {
		//	override superclass method
		// take no action
	}

	openNewFile {
		// get path/filename
		Dialog.openPanel({ arg path;
			// check for valid file
			if (this.isValidSoundFile(path), {
				// assign name
				sampleFileName = path;
				if (sampleFileNameView.notNil, {
					if (sampleFileNameView.notClosed, {sampleFileNameView.string = sampleFileName;});
				});
				// cue file
				this.cueSample(path)
			});
		});
	}

	isValidSoundFile {arg argPath;  // check argument is a valide stereo path
		var holdFile, errorMessage;
		holdFile = SoundFile.new;
		if (holdFile.openRead(argPath), {
			if (holdFile.numChannels != 2, {
				errorMessage = "Error: file should be stereo. No. channels: " ++ holdFile.numChannels.asString;
			});
		}, {
			errorMessage = "Error: invalid file " ++ argPath;
		});
		holdFile.close;
		if (errorMessage.notNil, {
			TXInfoScreen(errorMessage);
			^false;
		});
		^true;
	}

	cueSample { arg argFileName; // method to cue sample into buffer
		var holdBuffer;
		// check if valid filename
		if ((argFileName != "") and: (argFileName.pathMatch.size > 0), {
			sampleFileName = "";
			sampleNumChannels = 0;
			// close first before cueing
			if (buffers.at(0).notNil, {buffers.at(0).close});
			Routine.run {
				var holdModCondition;
				// add condition to load queue
				holdModCondition = system.holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				system.server.sync;
				if (argFileName.isNil, {
					// if argFileName is nil, clear the current buffer & filename
					buffers.at(0).zero;
				},{
					// otherwise,  try to cue file.  if it fails, display error message and clear
					holdBuffer = Buffer.cueSoundFile(system.server, argFileName, 0, 2, 32768, {
						arg argBuffer;
						//	if file loaded ok
						if (argBuffer.notNil, {
							sampleFileName = argFileName;
							sampleNumChannels = argBuffer.numChannels;
							buffers.put(0, argBuffer);
							// store current data to synthArgSpecs
							this.setSynthArgSpec("bufnumSound", argBuffer.bufnum);
						},{
							buffers.at(0).zero;
							{TXInfoScreen.new("File not found: " ++ argFileName);}.defer; // defer because gui process
						});
					});
				});
				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdModCondition);
				// update view
				system.showView;
			};
		},{
			{TXInfoScreen.new("File not found: " ++ argFileName);}.defer; // defer because gui process
		});
	} // end of method cueSample

	reCueSample { // method to re-cue sample into buffer from start of file
		// check if valid filename
		if (sampleFileName != "", {
			// close first before cueing
			buffers.at(0).close;
			buffers.at(0).cueSoundFile(sampleFileName,0);
		});
	} // end of method reCueSample

	syncStartPlayer {
		// if syncStart is 1 then start Player
		if (this.getSynthArgSpec("syncStart") == 1, {
			moduleNode.release(0);
			this.reCueSample;
			this.createSynthNote(60, 127, 0);
		});
	}

	syncStopPlayer {
		// if syncStop is 1 then stop Player
		if (this.getSynthArgSpec("syncStop") == 1, {
			moduleNode.release;
		});
	}

	stopPlayer {
		moduleNode.release;
	}

}

