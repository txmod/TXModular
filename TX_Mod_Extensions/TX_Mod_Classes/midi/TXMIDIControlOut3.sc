// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIControlOut3 : TXModuleBase { 

	classvar <>arrInstances;	
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels 
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs 
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels 
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=500;
	
	var	midiControlResp;
	var	sendTrigID;
	var	holdPortNames, arrSoloTexts;
	var	holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;

*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Control Out";
	moduleRate = "control";
	moduleType = "action";
	noInChannels = 0;			
	arrCtlSCInBusSpecs = [
		["Controller val", 1, "modControlVal", 0],
	];	
	noOutChannels = 1;
	arrOutBusSpecs = [ 
		["Out", [0]]
	];	
} 

*new{ arg argInstName;
	 ^super.new.init(argInstName);
} 

*soloOffAll{
	 arrInstances.do({arg item, i; item.setSynthValue("soloStatus", 0);});
} 

*soloOnMuteOthers{
	 arrInstances.do({arg item, i; item.setSynthValue("soloStatus", 2);});
} 

*soloOnMuteAll{
	 arrInstances.do({arg item, i; item.setSynthValue("soloStatus", 3);});
} 

*portCopyAll {arg holdPort;
	 arrInstances.do({arg item, i; item.setSynthValue("i_midiPort", holdPort); item.midiControlActivate; 
	 	item.initMidiPort(holdPort); item.rebuildSynth;});
} 

*dataRateCopyAll {arg holdRate;
	arrInstances.do({arg item, i; item.setSynthOption(0, holdRate); item.midiControlActivate; item.rebuildSynth;});
} 

*activeOnAll {
	arrInstances.do({arg item, i; item.setSynthValue("on", 1);});
} 

*activeOffAll {
	arrInstances.do({arg item, i; item.setSynthValue("on", 0);});
} 
init {arg argInstName;

	//	set  class specific instance variables
	arrSoloTexts = [
		"Solo Mode is Off (default setting)",
		"Solo Mode is On for this module, so nothing will be output from other Midi Control Out modules",
		"Solo Mode is On for another Midi Control Out module so nothing will be output from this module",
		"Solo Mute All is On so nothing will be output from any Midi Control Out modules"
	];

	// create unique id
	sendTrigID = UniqueID.next;
	arrSynthArgSpecs = [
		["out", 0, 0],
		["on", 0, 0],
		["i_midiPort", 0, 0],
		["i_midiChannel", 1, 0],
		["i_midiControlNo", 0, 0],
		["midiControlValue", 0, 0],
		["soloStatus",0,0],
		["modControlVal", 0, 0],
	]; 
	arrOptions = [4];
	arrOptionData = [
		[
			["1 time per second", 1],
			["3 times per second", 3],
			["5 times per second", 5],
			["10 times per second", 10],
			["20 times per second - default", 20],
			["40 times per second", 40],
			["80 times per second", 80],
		],
	];
	synthDefFunc = { arg out, on, i_midiPort, i_midiChannel, i_midiControlNo, midiControlValue,
		soloStatus, modControlVal;
	   var trig, trig2, sumControl, outval, dataRate, soloVal;
	   // select datarate based on arrOptions
	   dataRate = this.getSynthOption(0);
	   sumControl = (midiControlValue/127 + modControlVal).max(0).min(1);
	   outval = Lag.kr(sumControl * 127, (2 * dataRate).reciprocal).round;	   
	   // trigger current value to be sent every sec and when value changes
	   trig = Trig.kr((1 - Impulse.kr(dataRate)) * HPZ1.kr(outval).abs, 0.005); 
	   trig2 = Impulse.kr(1);
	   soloVal = (2 - soloStatus).min(1);
	   SendTrig.kr( (trig + trig2) * on * soloVal, sendTrigID, outval);
	   // Note this synth doesn't need to write to the output bus
	};
	holdPortNames = ["Unconnected - select MIDI Port"]
		++ MIDIClient.destinations.collect({arg item, i; 
			// remove any brackets from string
			(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
		});
	guiSpecArray = [
		["TXPopupAction", "Port", holdPortNames, "i_midiPort", 
			{ arg view; this.initMidiPort(view.value); }], 
		["NextLine"], 
		["SynthOptionPopup", "Data rate", arrOptionData, 0], 
		["SpacerLine", 4], 
		["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "i_midiChannel"], 
		["EZSlider", "Controller no", ControlSpec(0, 127, step: 1), "i_midiControlNo"], 
		["EZSlider", "Controller val", ControlSpec(0, 127, step: 1), "midiControlValue"], 
		["SpacerLine", 6], 
		["TXCheckBox", "Active", "on"],
		["SpacerLine", 8], 

		["TextBar", "Global Controls - these affect all Midi Control Out modules:", 400],
		["SpacerLine", 4], 
		["ActionButton", "Turn Off Solo Mode for all Midi Control Out modules", 
			{this.class.soloOffAll; system.flagGuiUpd;}, 400],
		["NextLine"], 
		["ActionButton", "Turn On Solo Mode for current Midi Control Out module only", 
			{this.class.soloOnMuteOthers; this.setSynthValue("soloStatus", 1); system.flagGuiUpd;}, 
			400, nil, TXColour.sysGuiCol2],
		["ActionButton", "Turn On Solo Mute All to mute all Midi Control Out modules", 
			{this.class.soloOnMuteAll; system.flagGuiUpd;},  
			400, nil, TXColour.sysDeleteCol],
		["NextLine"], 
		["TextViewDisplay", {arrSoloTexts.at(this.getSynthArgSpec("soloStatus").asInteger);}, nil, 40],

		["SpacerLine", 4], 
		["ActionButton", "Copy current Port to all Midi Control Out modules", 
			{this.class.portCopyAll(this.getSynthArgSpec("i_midiPort")); system.flagGuiUpd;}, 400],
		["NextLine"], 
		["ActionButton", "Copy current Data Rate to all Midi Control Out modules", 
			{this.class.dataRateCopyAll(this.getSynthOption(0)); system.flagGuiUpd;}, 400],
		["SpacerLine", 4], 
		["ActionButton", "Turn Active on for all Midi Control Out modules", 
			{this.class.activeOnAll; system.flagGuiUpd;}, 
			400, nil, TXColour.sysGuiCol2],
		["NextLine"], 
		["ActionButton", "Turn Active off for all Midi Control Out modules", 
			{this.class.activeOffAll; system.flagGuiUpd;}, 
			400, nil, TXColour.sysDeleteCol],
	];
	arrActionSpecs = this.buildActionSpecs([
		["TXPopupAction", "Port", holdPortNames, "i_midiPort", 
			{ arg view; this.initMidiPort(view.value); }], 
		["SynthOptionPopup", "Data rate", arrOptionData, 0], 
		["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "i_midiChannel"], 
		["EZSlider", "Controller no", ControlSpec(0, 127, step: 1), "i_midiControlNo"], 
		["EZSlider", "Controller val", ControlSpec(0, 127, step: 1), "midiControlValue"], 
		["TXCheckBox", "Active", "on"],
		["TextBar", "Global Controls - these affect all Midi Control Out modules:", 400],

		["commandAction", "Copy current Port to all Midi Control Out modules", 
			{this.class.portCopyAll(this.getSynthArgSpec("i_midiPort"));}],
		["commandAction", "Copy current Data Rate to all Midi Control Out modules", 
			{this.class.dataRateCopyAll(this.getSynthOption(0));}],
		["commandAction", "Turn Active on for all Midi Control Out modules", 
			{this.class.activeOnAll; system.flagGuiUpd;}],
		["commandAction", "Turn Active off for all Midi Control Out modules", 
			{this.class.activeOffAll; system.flagGuiUpd;}],
		["commandAction", "Turn Off Solo Mode for all Midi Control Out modules", 
			{this.class.soloOffAll; system.flagGuiUpd;}],
		["commandAction", "Turn On Solo Mode for current Midi Control Out module only", 
			{this.class.soloOnMuteOthers; this.setSynthValue("soloStatus", 1);}],
		["commandAction", "Turn On Solo Mute All to mute all Midi Control Out modules", 
			{this.class.soloOnMuteAll; system.flagGuiUpd;}],
		["TextViewDisplay", {arrSoloTexts.at(this.getSynthArgSpec("soloStatus").asInteger);}, 
			nil, 40, "SoloStatusText"],

	]);	
	//	use base class initialise 
	this.baseInit(this, argInstName);
	this.midiControlActivate;
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

initMidiPort { arg portInd = 0;
	if (portInd == 0, {
		holdMIDIOutPort = nil;
		holdMIDIDeviceName = nil;
		holdMIDIPortName = nil;
	},{
		holdMIDIOutPort = MIDIOut(portInd, MIDIClient.destinations[portInd - 1].uid);
		holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
		holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
		// minimise MIDI out latency
		holdMIDIOutPort.latency = 0;
	});
}

midiControlActivate {
	//	remove any previous OSCresponderNode and add new
	this.midiControlDeactivate;
	midiControlResp = OSCresponderNode(system.server.addr,'/tr',{ arg time,responder,msg;
		var portInd, channel, controlNo;
		if (msg[2] == sendTrigID,{
			portInd = this.getSynthArgSpec("i_midiPort");
			if ( portInd > 0, {
				channel = this.getSynthArgSpec("i_midiChannel");
				controlNo = this.getSynthArgSpec("i_midiControlNo");
				holdMIDIOutPort.control (channel-1, controlNo, msg[3]);
			});
		});
	}).add;
}

extraSaveData { // override default method
	^[holdMIDIDeviceName, holdMIDIPortName];
}

loadExtraData {arg argData;  // override default method
	var portIndex;
	portIndex = this.getSynthArgSpec("i_midiPort");
	holdMIDIDeviceName = argData.at(0);
	holdMIDIPortName = argData.at(1);
	// if names given, find correct port from names
	if ( holdMIDIDeviceName.notNil and: holdMIDIPortName.notNil, {
		// default to 0 in case device/port names not found
		portIndex = 0;
		this.setSynthArgSpec("i_midiPort", 0);
		MIDIClient.destinations.do({arg item, i;
			if ( (holdMIDIDeviceName == item.device) and: (holdMIDIPortName == item.name), {
				portIndex = i + 1;
				this.setSynthArgSpec("i_midiPort", portIndex);
			});
		});
	});
	this.initMidiPort(portIndex);
}

midiControlDeactivate { 
	//	remove responder 
	midiControlResp.remove;
}

deleteModuleExtraActions {     
	//	remove OSCresponderNoder
	this.midiControlDeactivate;
	// if this module is being soloed, then turn solo off
	if (this.getSynthArgSpec("soloStatus") == 1, {this.class.soloOffAll;});
}

}

