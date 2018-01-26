// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXEnvPresets {

	*initClass{
		//
		// set class specific variables
		//
	}

	// startEnvFunc used by audio source & insert modules to fade start & end of audio synth

	*startEnvFunc {
		// OLD:
		// ^{EnvGen.kr(Env.new([0, 0, 1], [0.1,0.1]), 1);};
		// NEW with gate for fade in & out
		^{EnvGen.kr(
			// Env.new([0, 0, 1, 1, 0], [0.1, 0.1, 0.1, 0.1], 'lin', 2), NamedControl.kr("gate", 1))
			Env.new([0, 0, 1, 1, 0], [0.1, 0.2, 0.1, 0.2], 'sine', 2), NamedControl.kr("gate", 1))
		};
	}

	// env presets for DADSR & DADSSR envelopes, sustain2 is ignored for DADSR
	// suffix used in modules with more than 1 env

	*arrEnvPresets { arg argModule, argSynthOptSlope, argSynthOptEnvType;
		^this.arrEnvPresetsSfx(argModule, argSynthOptSlope, argSynthOptEnvType, "");
	}

	*arrMonoEnvPresets { arg argModule, argSynthOptSlope, argSynthOptEnvType;
		^this.arrEnvPresetsSfx(argModule, argSynthOptSlope, argSynthOptEnvType, "", 1); // fixed length
	}

	*arrEnvPresets2 { arg argModule, argSynthOptSlope, argSynthOptEnvType;
		^this.arrEnvPresetsSfx(argModule, argSynthOptSlope, argSynthOptEnvType, "2");
	}

	*arrEnvPresetsEnv2 { arg argModule, argSynthOptSlope, argSynthOptEnvType;
		^this.arrEnvPresetsSfx(argModule, argSynthOptSlope, argSynthOptEnvType, "Env2");
	}

	*arrEnvPresetsSfx { arg argModule, argSynthOptSlope, argSynthOptEnvType, argSuffix, argEnvTypeVal = 0;
		^[
			[	"Percussive 0.1",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.1));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0);
					argModule.setSynthValue("sustain2" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.1));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.1));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 15);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Percussive 0.5",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.5));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0);
					argModule.setSynthValue("sustain2" ++ argSuffix, 0);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.5));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 15);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Organ - no attack or release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, 0.001);
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustain2" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(1));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 0);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 0.5 - slow attack & release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustain2" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(1.5));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 2);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 1.5 - slower attack and release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0.01, 5).unmap(1.5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustain2" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(2.5));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(1.5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 2);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Swell 5 - very slow attack and release",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0.01, 5).unmap(5));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, 0.05);
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 1);
					argModule.setSynthValue("sustain2" ++ argSuffix, 1);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 10).unmap(7));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 10);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(5));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 2);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			],
			[	"Piano - percussive start with sustain",
				{	argModule.setSynthValue("delay" ++ argSuffix, 0);
					argModule.setSynthValue("attack" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.05));
					argModule.setSynthValue("attackMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("attackMax" ++ argSuffix, 5);
					argModule.setSynthValue("decay" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.15));
					argModule.setSynthValue("decayMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("decayMax" ++ argSuffix, 5);
					argModule.setSynthValue("sustain" ++ argSuffix, 0.6);
					argModule.setSynthValue("sustain2" ++ argSuffix, 0.2);
					argModule.setSynthValue("sustainTime" ++ argSuffix, ControlSpec(0.01, 5).unmap(3));
					argModule.setSynthValue("sustainTimeMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("sustainTimeMax" ++ argSuffix, 5);
					argModule.setSynthValue("release" ++ argSuffix, ControlSpec(0.01, 5).unmap(0.1));
					argModule.setSynthValue("releaseMin" ++ argSuffix, 0.01);
					argModule.setSynthValue("releaseMax" ++ argSuffix, 5);
					argModule.arrOptions.put(argSynthOptSlope, 15);
					argModule.arrOptions.put(argSynthOptEnvType, argEnvTypeVal);
					argModule.rebuildSynth;
				},
			]
		];
	}

}

