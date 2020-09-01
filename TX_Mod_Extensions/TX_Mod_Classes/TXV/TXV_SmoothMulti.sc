// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// enum TXSMOOTHTYPE {TXSMOOTHTYPE_EXPONENTIAL, TXSMOOTHTYPE_LINEAR};
// add text for "smoothType/fixedValue"
// , ["Exponential", "Linear"]

TXV_SmoothMulti : TXV_Module {
	classvar <defaultName = "TXV SmoothMulti";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXSmoothMulti_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXSmoothMulti_1/smoothType", [
			["int", "/TXSmoothMulti_1/smoothType/fixedValue", 0, 0, 1, ["Exponential", "Linear"]],
			["float", "/TXSmoothMulti_1/smoothType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSmoothMulti_1/smoothType/useExtMod", 0],
			["float", "/TXSmoothMulti_1/smoothType/extModValue", 0, 0, 1],
			["int", "/TXSmoothMulti_1/smoothType/softMin", 0, 0, 1],
			["int", "/TXSmoothMulti_1/smoothType/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/smoothTime", [
			["float", "/TXSmoothMulti_1/smoothTime/fixedValue", 1, 0, 10],
			["float", "/TXSmoothMulti_1/smoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSmoothMulti_1/smoothTime/useExtMod", 0],
			["float", "/TXSmoothMulti_1/smoothTime/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/smoothTime/softMin", 0, 0, 60],
			["float", "/TXSmoothMulti_1/smoothTime/softMax", 10, 0, 60],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue1", [
			["float", "/TXSmoothMulti_1/targetValue1/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue1/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue1/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue1/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue1/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue1/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing1", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing1/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing1/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing1/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing1/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue2", [
			["float", "/TXSmoothMulti_1/targetValue2/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue2/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue2/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue2/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue2/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue2/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing2", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing2/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing2/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing2/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing2/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue3", [
			["float", "/TXSmoothMulti_1/targetValue3/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue3/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue3/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue3/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue3/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue3/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing3", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing3/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing3/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing3/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing3/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue4", [
			["float", "/TXSmoothMulti_1/targetValue4/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue4/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue4/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue4/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue4/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue4/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing4", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing4/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing4/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing4/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing4/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue5", [
			["float", "/TXSmoothMulti_1/targetValue5/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue5/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue5/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue5/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue5/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue5/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing5", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing5/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing5/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing5/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing5/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue6", [
			["float", "/TXSmoothMulti_1/targetValue6/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue6/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue6/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue6/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue6/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue6/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing6", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing6/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing6/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing6/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing6/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue7", [
			["float", "/TXSmoothMulti_1/targetValue7/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue7/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue7/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue7/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue7/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue7/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing7", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing7/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing7/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing7/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing7/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue8", [
			["float", "/TXSmoothMulti_1/targetValue8/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue8/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue8/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue8/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue8/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue8/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing8", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing8/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing8/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing8/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing8/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue9", [
			["float", "/TXSmoothMulti_1/targetValue9/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue9/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue9/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue9/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue9/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue9/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing9", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing9/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing9/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing9/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing9/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue10", [
			["float", "/TXSmoothMulti_1/targetValue10/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue10/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue10/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue10/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue10/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue10/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing10", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing10/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing10/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing10/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing10/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue11", [
			["float", "/TXSmoothMulti_1/targetValue11/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue11/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue11/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue11/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue11/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue11/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing11", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing11/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing11/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing11/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing11/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSmoothMulti_1/targetValue12", [
			["float", "/TXSmoothMulti_1/targetValue12/fixedValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue12/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSmoothMulti_1/targetValue12/useExtMod", 0],
			["float", "/TXSmoothMulti_1/targetValue12/extModValue", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue12/softMin", 0, 0, 1],
			["float", "/TXSmoothMulti_1/targetValue12/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSmoothMulti_1/circularSmoothing12", [
			["bool_int", "/TXSmoothMulti_1/circularSmoothing12/fixedValue", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing12/fixedModMix", 0],
			["bool_int", "/TXSmoothMulti_1/circularSmoothing12/useExtMod", 0],
			["float", "/TXSmoothMulti_1/circularSmoothing12/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXSmoothMulti_1 ---------- */
		"out1",
		"out2",
		"out3",
		"out4",
		"out5",
		"out6",
		"out7",
		"out8",
		"out9",
		"out10",
		"out11",
		"out12",
	];}

}


