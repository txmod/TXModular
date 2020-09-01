// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBusMainOuts : TXModuleBase {
	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiHeight=0;
	classvar <guiWidth=0;
	classvar <guiLeft=0;
	classvar <guiTop=0;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Main Outs";
		moduleRate = "audio";
		moduleType = "bus";
		noOutChannels = 16;
		arrOutBusSpecs = [
			["Out 1 + 2", [0,1]],
			["Out 3 + 4", [2,3]],
			["Out 5 + 6", [4,5]],
			["Out 7 + 8", [6,7]],
			["Out 9 + 10", [8,9]],
			["Out 11 + 12", [10,11]],
			["Out 13 + 14", [12,13]],
			["Out 15 + 16", [14,15]],
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
			["Out 16", [15]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	hardwire bus to physical audio outputs
		outBus = Bus.new(moduleRate.asSymbol, 0, noOutChannels);
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	baseOpenGui{
		// this is a dummy method to replace method in  TXModuleBase.
	}

}

TXBusFXSend : TXModuleBase {
	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiHeight=0;
	classvar <guiWidth=0;
	classvar <guiLeft=0;
	classvar <guiTop=0;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "FX Send";
		moduleRate = "audio";
		moduleType = "bus";
		noOutChannels = 1;
		arrOutBusSpecs = [
			["Bus ", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	baseOpenGui{
		// this is a dummy method to replace method in  TXModuleBase.
	}

}

TXBusAudioAux : TXModuleBase {
	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiHeight=0;
	classvar <guiWidth=0;
	classvar <guiLeft=0;
	classvar <guiTop=0;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Audio Aux";
		moduleRate = "audio";
		moduleType = "bus";
		noOutChannels = 2;
		arrOutBusSpecs = [
			["Bus L + R", [0,1]],
			["Bus L only", [0]],
			["Bus R only", [1]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	baseOpenGui{
		// this is a dummy method to replace method in  TXModuleBase.
	}

}

TXBusControlAux : TXModuleBase {
	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <guiHeight=0;
	classvar <guiWidth=0;
	classvar <guiLeft=0;
	classvar <guiTop=0;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Control Aux";
		moduleRate = "control";
		moduleType = "bus";
		noOutChannels = 1;
		arrOutBusSpecs = [
			["Bus ", [1]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	baseOpenGui{
		// this is a dummy method to replace method in  TXModuleBase.
	}

}
