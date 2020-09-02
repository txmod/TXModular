// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// add text for "lfoType/fixedValue"
// , ["Sine", "Cosine", "Square", "Pulse", "Triangle", "Saw", "Saw Reversed", "Random", "Random Quantised", "Random Smoothed", "Double Random", "Double Random Quantised", "Double Random Smoothed", "Trefoil Knot X", "Trefoil Knot Y", "Trefoil Knot Z", "Custom Curve", "Custom Curve - Mirrored"]

// add text for "randomSmoothType/fixedValue"
//, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]

TXV_LFO : TXV_Module {
	classvar <defaultName = "TXV LFO";
	classvar <>arrInstances;
	var <>arrSlotData;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXLFO_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXLFO_1/lfoType", [
			["int", "/TXLFO_1/lfoType/fixedValue", 0, 0, 17, ["Sine", "Cosine", "Square", "Pulse", "Triangle", "Saw", "Saw Reversed", "Random", "Random Quantised", "Random Smoothed", "Double Random", "Double Random Quantised", "Double Random Smoothed", "Trefoil Knot X", "Trefoil Knot Y", "Trefoil Knot Z", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXLFO_1/lfoType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/lfoType/useExtMod", 0],
			["float", "/TXLFO_1/lfoType/extModValue", 0, 0, 1],
			["int", "/TXLFO_1/lfoType/softMin", 0, 0, 17],
			["int", "/TXLFO_1/lfoType/softMax", 17, 0, 17],
		]],
		[ "modParameterGroupInt", "/TXLFO_1/randomSmoothType", [
			["int", "/TXLFO_1/randomSmoothType/fixedValue", 23, 0, 23, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXLFO_1/randomSmoothType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/randomSmoothType/useExtMod", 0],
			["float", "/TXLFO_1/randomSmoothType/extModValue", 0, 0, 1],
			["int", "/TXLFO_1/randomSmoothType/softMin", 0, 0, 23],
			["int", "/TXLFO_1/randomSmoothType/softMax", 23, 0, 23],
		]],
		[ "modParameterGroupFloat", "/TXLFO_1/bpm", [
			["float", "/TXLFO_1/bpm/fixedValue", 120, 0.1, 200],
			["float", "/TXLFO_1/bpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/bpm/useExtMod", 0],
			["float", "/TXLFO_1/bpm/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/bpm/softMin", 0.1, 0.001, 1000.0],
			["float", "/TXLFO_1/bpm/softMax", 200, 0.001, 1000.0],
		]],
		[ "modParameterGroupBool", "/TXLFO_1/useMasterBpm", [
			["bool_int", "/TXLFO_1/useMasterBpm/fixedValue", 1],
			["bool_int", "/TXLFO_1/useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXLFO_1/useMasterBpm/useExtMod", 0],
			["float", "/TXLFO_1/useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLFO_1/cycleTimeBeats", [
			["float", "/TXLFO_1/cycleTimeBeats/fixedValue", 1, 1, 64],
			["float", "/TXLFO_1/cycleTimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/cycleTimeBeats/useExtMod", 0],
			["float", "/TXLFO_1/cycleTimeBeats/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/cycleTimeBeats/softMin", 1, 0.01, 1000],
			["float", "/TXLFO_1/cycleTimeBeats/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupFloat", "/TXLFO_1/cycleTimeDivisor", [
			["float", "/TXLFO_1/cycleTimeDivisor/fixedValue", 1, 1, 64],
			["float", "/TXLFO_1/cycleTimeDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/cycleTimeDivisor/useExtMod", 0],
			["float", "/TXLFO_1/cycleTimeDivisor/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/cycleTimeDivisor/softMin", 1, 0.01, 1000],
			["float", "/TXLFO_1/cycleTimeDivisor/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupFloat", "/TXLFO_1/phaseOffset", [
			["float", "/TXLFO_1/phaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXLFO_1/phaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/phaseOffset/useExtMod", 0],
			["float", "/TXLFO_1/phaseOffset/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/phaseOffset/softMin", 0, 0, 1],
			["float", "/TXLFO_1/phaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLFO_1/pulseWidth", [
			["float", "/TXLFO_1/pulseWidth/fixedValue", 0.5, 0, 1],
			["float", "/TXLFO_1/pulseWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/pulseWidth/useExtMod", 0],
			["float", "/TXLFO_1/pulseWidth/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/pulseWidth/softMin", 0, 0, 1],
			["float", "/TXLFO_1/pulseWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLFO_1/frozen", [
			["bool_int", "/TXLFO_1/frozen/fixedValue", 0],
			["bool_int", "/TXLFO_1/frozen/fixedModMix", 0],
			["bool_int", "/TXLFO_1/frozen/useExtMod", 0],
			["float", "/TXLFO_1/frozen/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLFO_1/resetClock", [
			["bool_int", "/TXLFO_1/resetClock/fixedValue", 0],
			["bool_int", "/TXLFO_1/resetClock/fixedModMix", 0],
			["bool_int", "/TXLFO_1/resetClock/useExtMod", 0],
			["float", "/TXLFO_1/resetClock/extModValue", 0, 0, 1],
		]],
		["float256", "/TXLFO_1/customCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXLFO_1/customCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXLFO_1/customCurveMorph", [
			["float", "/TXLFO_1/customCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXLFO_1/customCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLFO_1/customCurveMorph/useExtMod", 0],
			["float", "/TXLFO_1/customCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXLFO_1/customCurveMorph/softMin", 0, 0, 1],
			["float", "/TXLFO_1/customCurveMorph/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXLFO_1 ---------- */
		"out",
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["BPM, Cycle, Phase", {arg parameterName;
				parameterName.containsi("bpm")
				|| parameterName.containsi("cycleTime")
				|| parameterName.containsi("phaseOffset")
				|| parameterName.containsi("frozen")
				|| parameterName.containsi("resetClock")
				;}],
			["Custom Curve", {arg parameterName;
				parameterName.containsi("customCurve");}],
		];
	}

	initExtraActions { // override
		// arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
	}

	extraSubModuleData { // override
		^[arrSlotData];
	}

	loadExtraSubModuleData {arg argData;  // override
		arrSlotData = argData[0];
	}

	getExtraSynthArgSpecs { //  override
		^[
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
	}

}


