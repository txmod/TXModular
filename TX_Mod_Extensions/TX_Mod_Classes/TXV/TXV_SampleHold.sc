// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_SampleHold : TXV_Module {
	classvar <defaultName = "TXV Sample & Hold";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXSampleHold_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupFloat", "/TXSampleHold_1/inputValue", [
			["float", "/TXSampleHold_1/inputValue/fixedValue", 0, 0, 1],
			["float", "/TXSampleHold_1/inputValue/fixedModMix", 1, 0, 1],
			["bool_int", "/TXSampleHold_1/inputValue/useExtMod", 0],
			["float", "/TXSampleHold_1/inputValue/extModValue", 0, 0, 1],
			["float", "/TXSampleHold_1/inputValue/softMin", 0, 0, 1],
			["float", "/TXSampleHold_1/inputValue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSampleHold_1/hold", [
			["bool_int", "/TXSampleHold_1/hold/fixedValue", 0],
			["bool_int", "/TXSampleHold_1/hold/fixedModMix", 0],
			["bool_int", "/TXSampleHold_1/hold/useExtMod", 0],
			["float", "/TXSampleHold_1/hold/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSampleHold_1/bypass", [
			["bool_int", "/TXSampleHold_1/bypass/fixedValue", 0],
			["bool_int", "/TXSampleHold_1/bypass/fixedModMix", 0],
			["bool_int", "/TXSampleHold_1/bypass/useExtMod", 0],
			["float", "/TXSampleHold_1/bypass/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXSampleHold_1 ---------- */
		"out",
	];}

}


