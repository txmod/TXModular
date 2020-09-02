// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// enum TXWARPTYPE {TXWARPTYPE_LINEAR, TXWARPTYPE_EXPONENTIAL, TXWARPTYPE_MINUS_EXPONENTIAL, TXWARPTYPE_5, TXWARPTYPE_4, TXWARPTYPE_3, TXWARPTYPE_2, TXWARPTYPE_MINUS_2, TXWARPTYPE_MINUS_3, TXWARPTYPE_MINUS_4, TXWARPTYPE_MINUS_5, TXWARPTYPE_PHASESHIFT_90, TXWARPTYPE_PHASESHIFT_180, TXWARPTYPE_PHASESHIFT_270 };
// add text for "warpType/fixedValue"
// , ["Linear", "Exponential",  "Minus Exponential", "Curve 5", "Curve 4", "Curve 3", "Curve 2", "Curve Minus 2", "Curve Minus 3", "Curve Minus 4", "Curve Minus 5", "Phase Shift 90", "Phase Shift 180", "Phase Shift 270", "Custom Curve", ]

TXV_Warp : TXV_Module {
	classvar <defaultName = "TXV Warp";
	classvar <>arrInstances;
	var <>arrSlotData;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXWarp_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupFloat", "/TXWarp_1/inputValue", [
			["float", "/TXWarp_1/inputValue/fixedValue", 0, 0, 1],
			["float", "/TXWarp_1/inputValue/fixedModMix", 1, 0, 1],
			["bool_int", "/TXWarp_1/inputValue/useExtMod", 0],
			["float", "/TXWarp_1/inputValue/extModValue", 0, 0, 1],
			["float", "/TXWarp_1/inputValue/softMin", 0, 0, 1],
			["float", "/TXWarp_1/inputValue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXWarp_1/warpType", [
			["int", "/TXWarp_1/warpType/fixedValue", 0, 0, 14,
				["Linear", "Exponential",  "Minus Exponential", "Curve 5", "Curve 4", "Curve 3", "Curve 2", "Curve Minus 2", "Curve Minus 3", "Curve Minus 4", "Curve Minus 5", "Phase Shift 90", "Phase Shift 180", "Phase Shift 270", "Custom Curve",]],
			["float", "/TXWarp_1/warpType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/warpType/useExtMod", 0],
			["float", "/TXWarp_1/warpType/extModValue", 0, 0, 1],
			["int", "/TXWarp_1/warpType/softMin", 0, 0, 14],
			["int", "/TXWarp_1/warpType/softMax", 14, 0, 14],
		]],
		[ "modParameterGroupFloat", "/TXWarp_1/inputMin", [
			["float", "/TXWarp_1/inputMin/fixedValue", 0, 0, 1],
			["float", "/TXWarp_1/inputMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/inputMin/useExtMod", 0],
			["float", "/TXWarp_1/inputMin/extModValue", 0, 0, 1],
			["float", "/TXWarp_1/inputMin/softMin", 0, 0, 1],
			["float", "/TXWarp_1/inputMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWarp_1/inputMax", [
			["float", "/TXWarp_1/inputMax/fixedValue", 1, 0, 1],
			["float", "/TXWarp_1/inputMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/inputMax/useExtMod", 0],
			["float", "/TXWarp_1/inputMax/extModValue", 0, 0, 1],
			["float", "/TXWarp_1/inputMax/softMin", 0, 0, 1],
			["float", "/TXWarp_1/inputMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWarp_1/outputMin", [
			["float", "/TXWarp_1/outputMin/fixedValue", 0, 0, 1],
			["float", "/TXWarp_1/outputMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/outputMin/useExtMod", 0],
			["float", "/TXWarp_1/outputMin/extModValue", 0, 0, 1],
			["float", "/TXWarp_1/outputMin/softMin", 0, 0, 1],
			["float", "/TXWarp_1/outputMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWarp_1/outputMax", [
			["float", "/TXWarp_1/outputMax/fixedValue", 1, 0, 1],
			["float", "/TXWarp_1/outputMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/outputMax/useExtMod", 0],
			["float", "/TXWarp_1/outputMax/extModValue", 0, 0, 1],
			["float", "/TXWarp_1/outputMax/softMin", 0, 0, 1],
			["float", "/TXWarp_1/outputMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXWarp_1/useQuantize", [
			["bool_int", "/TXWarp_1/useQuantize/fixedValue", 0],
			["bool_int", "/TXWarp_1/useQuantize/fixedModMix", 0],
			["bool_int", "/TXWarp_1/useQuantize/useExtMod", 0],
			["float", "/TXWarp_1/useQuantize/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXWarp_1/quantizeSteps", [
			["int", "/TXWarp_1/quantizeSteps/fixedValue", 4, 2, 20],
			["float", "/TXWarp_1/quantizeSteps/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWarp_1/quantizeSteps/useExtMod", 0],
			["float", "/TXWarp_1/quantizeSteps/extModValue", 0, 0, 1],
			["int", "/TXWarp_1/quantizeSteps/softMin", 2, 2, 128],
			["int", "/TXWarp_1/quantizeSteps/softMax", 20, 2, 128],
		]],
		["float256", "/TXWarp_1/customCurve", 0.0, 0.004, 0.008, 0.012, 0.016, 0.02, 0.024, 0.027, 0.031, 0.035, 0.039, 0.043, 0.047, 0.051, 0.055, 0.059, 0.063, 0.067, 0.071, 0.075, 0.078, 0.082, 0.086, 0.09, 0.094, 0.098, 0.102, 0.106, 0.11, 0.114, 0.118, 0.122, 0.125, 0.129, 0.133, 0.137, 0.141, 0.145, 0.149, 0.153, 0.157, 0.161, 0.165, 0.169, 0.173, 0.176, 0.18, 0.184, 0.188, 0.192, 0.196, 0.2, 0.204, 0.208, 0.212, 0.216, 0.22, 0.224, 0.227, 0.231, 0.235, 0.239, 0.243, 0.247, 0.251, 0.255, 0.259, 0.263, 0.267, 0.271, 0.275, 0.278, 0.282, 0.286, 0.29, 0.294, 0.298, 0.302, 0.306, 0.31, 0.314, 0.318, 0.322, 0.325, 0.329, 0.333, 0.337, 0.341, 0.345, 0.349, 0.353, 0.357, 0.361, 0.365, 0.369, 0.373, 0.376, 0.38, 0.384, 0.388, 0.392, 0.396, 0.4, 0.404, 0.408, 0.412, 0.416, 0.42, 0.424, 0.427, 0.431, 0.435, 0.439, 0.443, 0.447, 0.451, 0.455, 0.459, 0.463, 0.467, 0.471, 0.475, 0.478, 0.482, 0.486, 0.49, 0.494, 0.498, 0.502, 0.506, 0.51, 0.514, 0.518, 0.522, 0.525, 0.529, 0.533, 0.537, 0.541, 0.545, 0.549, 0.553, 0.557, 0.561, 0.565, 0.569, 0.573, 0.576, 0.58, 0.584, 0.588, 0.592, 0.596, 0.6, 0.604, 0.608, 0.612, 0.616, 0.62, 0.624, 0.627, 0.631, 0.635, 0.639, 0.643, 0.647, 0.651, 0.655, 0.659, 0.663, 0.667, 0.671, 0.675, 0.678, 0.682, 0.686, 0.69, 0.694, 0.698, 0.702, 0.706, 0.71, 0.714, 0.718, 0.722, 0.725, 0.729, 0.733, 0.737, 0.741, 0.745, 0.749, 0.753, 0.757, 0.761, 0.765, 0.769, 0.773, 0.776, 0.78, 0.784, 0.788, 0.792, 0.796, 0.8, 0.804, 0.808, 0.812, 0.816, 0.82, 0.824, 0.827, 0.831, 0.835, 0.839, 0.843, 0.847, 0.851, 0.855, 0.859, 0.863, 0.867, 0.871, 0.875, 0.878, 0.882, 0.886, 0.89, 0.894, 0.898, 0.902, 0.906, 0.91, 0.914, 0.918, 0.922, 0.925, 0.929, 0.933, 0.937, 0.941, 0.945, 0.949, 0.953, 0.957, 0.961, 0.965, 0.969, 0.973, 0.976, 0.98, 0.984, 0.988, 0.992, 0.996, 1.0 ],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXWarp_1 ---------- */
		"out",
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Quantize", {arg parameterName;
				parameterName.containsi("Quantize");}],
			["Custom Curve", {arg parameterName;
				parameterName.containsi("customCurve");}],
		];
	}

	initExtraActions { // override
		arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
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


