// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// enum TXSMOOTHTYPE {TXSMOOTHTYPE_EXPONENTIAL, TXSMOOTHTYPE_LINEAR};
// add text for "smoothType/fixedValue"
// , ["Exponential", "Linear"]

TXV_Smooth : TXV_Module {
	classvar <defaultName = "TXV Smooth";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXSmooth_1 ---------- */
		/* GROUP:     [groupType, address, ...parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXSmooth_1/smoothType", [
			["int", "/TXSmooth_1/smoothType/fixedValue", 0, 0, 1, ["Exponential", "Linear"]],
			["float", "/TXSmooth_1/smoothType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSmooth_1/smoothType/useExtMod", 0],
			["float", "/TXSmooth_1/smoothType/extModValue", 0, 0, 1],
			["int", "/TXSmooth_1/smoothType/softMin", 0, 0, 1],
			["int", "/TXSmooth_1/smoothType/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmooth_1/targetValue", [
			["float", "/TXSmooth_1/targetValue/fixedValue", 0, 0, 1],
			["float", "/TXSmooth_1/targetValue/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmooth_1/targetValue/useExtMod", 0],
			["float", "/TXSmooth_1/targetValue/extModValue", 0, 0, 1],
			["float", "/TXSmooth_1/targetValue/softMin", 0, 0, 1],
			["float", "/TXSmooth_1/targetValue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmooth_1/smoothTime", [
			["float", "/TXSmooth_1/smoothTime/fixedValue", 1, 0, 10],
			["float", "/TXSmooth_1/smoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSmooth_1/smoothTime/useExtMod", 0],
			["float", "/TXSmooth_1/smoothTime/extModValue", 0, 0, 1],
			["float", "/TXSmooth_1/smoothTime/softMin", 0, 0, 60],
			["float", "/TXSmooth_1/smoothTime/softMax", 10, 0, 60],
		]],
		[ "modParameterGroupBool", "/TXSmooth_1/circularSmoothing", [
			["bool_int", "/TXSmooth_1/circularSmoothing/fixedValue", 0],
			["bool_int", "/TXSmooth_1/circularSmoothing/fixedModMix", 0],
			["bool_int", "/TXSmooth_1/circularSmoothing/useExtMod", 0],
			["float", "/TXSmooth_1/circularSmoothing/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXSmooth_1 ---------- */
		"out",
	];}

}


