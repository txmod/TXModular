// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Toggle : TXV_Module {
	classvar <defaultName = "TXV Toggle";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXToggle_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupFloat", "/TXToggle_1/inputValue", [
			["float", "/TXToggle_1/inputValue/fixedValue", 0, 0, 1],
			["float", "/TXToggle_1/inputValue/fixedModMix", 1, 0, 1],
			["bool_int", "/TXToggle_1/inputValue/useExtMod", 0],
			["float", "/TXToggle_1/inputValue/extModValue", 0, 0, 1],
			["float", "/TXToggle_1/inputValue/softMin", 0, 0, 1],
			["float", "/TXToggle_1/inputValue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXToggle_1/triggerThreshold", [
			["float", "/TXToggle_1/triggerThreshold/fixedValue", 0.5, 0, 1],
			["float", "/TXToggle_1/triggerThreshold/fixedModMix", 0, 0, 1],
			["bool_int", "/TXToggle_1/triggerThreshold/useExtMod", 0],
			["float", "/TXToggle_1/triggerThreshold/extModValue", 0, 0, 1],
			["float", "/TXToggle_1/triggerThreshold/softMin", 0, 0, 1],
			["float", "/TXToggle_1/triggerThreshold/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/resetToZero", [
			["bool_int", "/TXStepSequencer_1/resetToZero/fixedValue", 0],
			["bool_int", "/TXStepSequencer_1/resetToZero/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/resetToZero/useExtMod", 0],
			["float", "/TXStepSequencer_1/resetToZero/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXToggle_1 ---------- */
		"out",
	];}

}


