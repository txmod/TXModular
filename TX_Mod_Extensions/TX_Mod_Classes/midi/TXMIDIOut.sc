// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIOut : TXModuleBase {

	classvar <>classData;

	var	holdMIDIOutTypes;
	var	holdPortNames;
	var	holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Midi Out";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		moduleNodeStatus = "running";

		arrSynthArgSpecs = [
			["midiPort", 0],
			["midiChannel", 1],
			["messageType", 0],
			["midiNote", 60],
			["midiVelocity", 100],
			["gateTime", 1.0],
			["midiControlNo", 0],
			["midiControlValue", 0],
			["midiProgramNo", 0],
			["midiPitchBend", 0],
			["midiPolyTouch", 0],
			["midiTouch", 0],
			["midiLoopBack", 0, 0],
		];

		holdMIDIOutTypes = [
			"Note On", // 0
			"Note Off", // 1
			"Note On & Off", // 2
			"Controller", // 3
			"Program", // 4
			"Pitch Bend", // 5
			"All Notes Off", // 6
			"Poly Touch", // 7
			"Touch", // 8
		];

		holdPortNames = ["Unconnected - select MIDI Port"]
		++ MIDIClient.destinations.collect({arg item, i;
			// remove any brackets from string
			(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
		});

		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Send MIDI Message Now", {this.sendMIDIMessage}],
			["TXPopupAction", "Port", holdPortNames, "midiPort", { arg view; this.initMidiPort(view.value); }],
			["TXCheckBox", "MIDI Loop-Back", "midiLoopBack", nil, 120],
			["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "midiChannel"],
			["TXPopupAction", "MIDI Message", holdMIDIOutTypes, "messageType",
				{ arg view; this.buildGuiSpecArray; system.showViewIfModDisplay(this); }],
			["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"],
			["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"],
			["EZSlider", "Gate Time", ControlSpec(0.01, 20), "gateTime"],
			["EZSlider", "Control no", ControlSpec(0, 127, step: 1), "midiControlNo"],
			["EZSlider", "Control val", ControlSpec(0, 127, step: 1), "midiControlValue"],
			["EZSlider", "Program No.", ControlSpec(0, 127, step: 1), "midiProgramNo"],
			["EZSlider", "Pitch Bend", ControlSpec(-8192, 8192, step: 1), "midiPitchBend"],
			["EZSlider", "Poly Touch", ControlSpec(0, 127, step: 1), "midiPolyTouch"],
			["EZSlider", "Touch", ControlSpec(0, 127, step: 1), "midiTouch"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	buildGuiSpecArray {
		var holdMessageType = this.getSynthArgSpec("messageType");
		guiSpecArray = [
			["SpacerLine", 6],
			["TXPopupAction", "Port", holdPortNames, "midiPort", { arg view; this.initMidiPort(view.value); }],
			["SpacerLine", 6],
			["TXCheckBox", "MIDI Loop-Back", "midiLoopBack", nil, 120],
			["SpacerLine", 6],
			["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "midiChannel"],
			["SpacerLine", 10],
			["TXPopupAction", "MIDI Message", holdMIDIOutTypes, "messageType",
				{ arg view; this.buildGuiSpecArray; system.showViewIfModDisplay(this); }],
		];
		// "Note On"
		case {holdMessageType == 0} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"],
				["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"],
			];
		}
		// "Note Off"
		{holdMessageType == 1} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"],
				["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"],
			];
		}
		// "Note On & Off"
		{holdMessageType == 2} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"],
				["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"],
				["EZSlider", "Gate Time", ControlSpec(0.01, 20), "gateTime"],
			];
		}
		// "Controller"
		{holdMessageType == 3} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Control no", ControlSpec(0, 127, step: 1), "midiControlNo"],
				["EZSlider", "Control val", ControlSpec(0, 127, step: 1), "midiControlValue"],
			];
		}
		// "Program"
		{holdMessageType == 4} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Program No.", ControlSpec(0, 127, step: 1), "midiProgramNo"],
			];
		}
		// "Pitch Bend"
		{holdMessageType == 5} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Pitch Bend", ControlSpec(-8192, 8192, step: 1), "midiPitchBend"],
			];
		}
		// "Poly Touch"
		{holdMessageType == 7} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"],
				["EZSlider", "Poly Touch", ControlSpec(0, 127, step: 1), "midiPolyTouch"],
			];
		}
		// "Touch"
		{holdMessageType == 8} {
			guiSpecArray = guiSpecArray ++[
				["EZSlider", "Touch", ControlSpec(0, 127, step: 1), "midiTouch"],
			];
		};
		// Action button
		guiSpecArray = guiSpecArray ++[
			["SpacerLine", 8],
			["ActionButton", "Send MIDI Message Now", {this.sendMIDIMessage;}, 250, TXColor.white, TXColor.sysGuiCol2],
		];
	}

	initMidiPort { arg portInd = 0;
		if (portInd == 0, {
			holdMIDIOutPort = nil;
			holdMIDIDeviceName = nil;
			holdMIDIPortName = nil;
		},{
			holdMIDIOutPort = MIDIOut(portInd - 1, MIDIClient.destinations[portInd - 1].uid);
			holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
			holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
			// minimise MIDI out latency
			holdMIDIOutPort.latency = 0;
		});
	}

	sendMIDIMessage {
		var holdMessageType, portInd, channel, midiLoopBack;
		holdMessageType = this.getSynthArgSpec("messageType");
		portInd = this.getSynthArgSpec("midiPort");
		channel = (this.getSynthArgSpec("midiChannel") - 1).asInteger;
		midiLoopBack = this.getSynthArgSpec("midiLoopBack");

		if (moduleNodeStatus == "running", {
			if (portInd > 0, {
				// "Note On"
				case {holdMessageType == 0} {
					holdMIDIOutPort.noteOn(channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiVelocity"));
				}
				// "Note Off"
				{holdMessageType == 1} {
					holdMIDIOutPort.noteOff(channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiVelocity"));
				}
				// "Note On & Off"
				{holdMessageType == 2} {
					SystemClock.schedAbs( 0,{
						holdMIDIOutPort.noteOn(channel, this.getSynthArgSpec("midiNote").asInteger,
							this.getSynthArgSpec("midiVelocity"));
					});
					SystemClock.sched( this.getSynthArgSpec("gateTime"),{
						holdMIDIOutPort.noteOff(channel, this.getSynthArgSpec("midiNote").asInteger, 0);
						nil;
					});
				}
				// "Controller"
				{holdMessageType == 3} {
					holdMIDIOutPort.control(channel, this.getSynthArgSpec("midiControlNo").asInteger,
						this.getSynthArgSpec("midiControlValue"));
				}
				// "Program"
				{holdMessageType == 4} {
					holdMIDIOutPort.program(channel, this.getSynthArgSpec("midiProgramNo").asInteger);
				}
				// "Pitch Bend"
				{holdMessageType == 5} {
					holdMIDIOutPort.bend(channel, this.getSynthArgSpec("midiPitchBend").asInteger);
				}
				// All Notes Off
				{holdMessageType == 6} {
					holdMIDIOutPort.allNotesOff(channel);
				}
				// "Poly Touch"
				{holdMessageType == 7} {
					holdMIDIOutPort.polyTouch(channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiPolyTouch"));
				}
				// "Touch"
				{holdMessageType == 8} {
					holdMIDIOutPort.touch(channel, this.getSynthArgSpec("midiTouch").asInteger);
				};
			});
			if (midiLoopBack == 1, {
				// "Note On"
				case {holdMessageType == 0} {
					MIDIIn.doNoteOnAction(0, channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiVelocity"));
				}
				// "Note Off"
				{holdMessageType == 1} {
					MIDIIn.doNoteOffAction(0, channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiVelocity"));
				}
				// "Note On & Off"
				{holdMessageType == 2} {
					SystemClock.schedAbs( 0,{
						MIDIIn.doNoteOnAction(0, channel, this.getSynthArgSpec("midiNote").asInteger,
							this.getSynthArgSpec("midiVelocity"));
					});
					SystemClock.sched( this.getSynthArgSpec("gateTime"),{
						MIDIIn.doNoteOffAction(0, channel, this.getSynthArgSpec("midiNote").asInteger,
							this.getSynthArgSpec("midiVelocity"));
						nil;
					});
				}
				// "Controller"
				{holdMessageType == 3} {
					MIDIIn.doControlAction(0, channel, this.getSynthArgSpec("midiControlNo").asInteger,
						this.getSynthArgSpec("midiControlValue"));
				}
				// "Program"
				{holdMessageType == 4} {
					MIDIIn.doProgramAction(0, channel, this.getSynthArgSpec("midiProgramNo").asInteger);
				}
				// "Pitch Bend"
				{holdMessageType == 5} {
					MIDIIn.doBendAction(0, channel, this.getSynthArgSpec("midiPitchBend").asInteger);
				}
				// "Poly Touch"
				{holdMessageType == 7} {
					MIDIIn.doPolyTouchAction(0, channel, this.getSynthArgSpec("midiNote").asInteger,
						this.getSynthArgSpec("midiPolyTouch"));
				}
			});
		});
	}
	extraSaveData { // override default method
		^[holdMIDIDeviceName, holdMIDIPortName];
	}

	loadExtraData {arg argData;  // override default method
		var portIndex;
		portIndex = this.getSynthArgSpec("midiPort");
		holdMIDIDeviceName = argData.at(0);
		holdMIDIPortName = argData.at(1);
		// if names given, find correct port from names
		if ( holdMIDIDeviceName.notNil and: holdMIDIPortName.notNil, {
			// default to 0 in case device/port names not found
			portIndex = 0;
			this.setSynthArgSpec("midiPort", 0);
			MIDIClient.destinations.do({arg item, i;
				if ( (holdMIDIDeviceName == item.device) and: (holdMIDIPortName == item.name), {
					portIndex = i + 1;
					this.setSynthArgSpec("midiPort", portIndex);
				});
			});
		});
		this.initMidiPort(portIndex);
		this.buildGuiSpecArray;
	}

	runAction {   //	override base class
		moduleNodeStatus = "running";
	}

	pauseAction {   //	override base class
		moduleNodeStatus = "paused";
	}

}

