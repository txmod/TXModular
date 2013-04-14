// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXEnvPresets {

	*initClass{
		//
		// set class specific variables
		//
	}


	*startEnvFunc {
		^{EnvGen.kr(Env.new([0, 0, 1], [0.1,0.1]), 1);};
	}

	*arrEnvPresets { arg argModule, argSynthOptEnvType, argSynthOptSlope;
		^this.arrEnvPresetsSfx(argModule, argSynthOptEnvType, argSynthOptSlope, "");
	}

	*arrEnvPresets2 { arg argModule, argSynthOptEnvType, argSynthOptSlope;
		^this.arrEnvPresetsSfx(argModule, argSynthOptEnvType, argSynthOptSlope, "2");
	}


	*arrEnvPresetsSfx { arg argModule, argSynthOptEnvType, argSynthOptSlope, argSuffix;
		^[
			[	"Percussive 0.1",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 15);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Percussive 0.5",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0, 5).unmap(0.5));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0, 5).unmap(0.5));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(0.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 15);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Organ - no attack or release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0.001);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 0);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 0.5 - slow attack & release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0, 5).unmap(0.5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(0.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 2);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 1.5 - slower attack and release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0, 5).unmap(1.5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(1.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 2);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 5 - very slow attack and release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0, 5).unmap(5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 2);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			],
			[	"Piano - percussive start with sustain",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0.5);
					argModule.setSynthValue("sustainTime" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0, 5).unmap(0.1));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptEnvType, 15);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.rebuildSynth;
				},
			]
		];
	}

}

