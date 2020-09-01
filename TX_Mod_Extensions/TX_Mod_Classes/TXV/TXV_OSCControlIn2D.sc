// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_OSCControlIn2D : TXV_Module {
	classvar <defaultName = "TXV OSC ControlIn2D";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXOSCControlIn2D_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXOSCControlIn2D_1/active", [
			["bool_int", "/TXOSCControlIn2D_1/active/fixedValue", 1],
			["bool_int", "/TXOSCControlIn2D_1/active/fixedModMix", 0],
			["bool_int", "/TXOSCControlIn2D_1/active/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/active/extModValue", 0, 0, 1],
		]],
		["string", "/TXOSCControlIn2D_1/oscAddress", "/Example/text"],
		[ "modParameterGroupFloat", "/TXOSCControlIn2D_1/input1Min", [
			["float", "/TXOSCControlIn2D_1/input1Min/fixedValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input1Min/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn2D_1/input1Min/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/input1Min/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input1Min/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn2D_1/input1Min/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn2D_1/input1Max", [
			["float", "/TXOSCControlIn2D_1/input1Max/fixedValue", 1, 0, 1],
			["float", "/TXOSCControlIn2D_1/input1Max/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn2D_1/input1Max/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/input1Max/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input1Max/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn2D_1/input1Max/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn2D_1/input2Min", [
			["float", "/TXOSCControlIn2D_1/input2Min/fixedValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input2Min/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn2D_1/input2Min/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/input2Min/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input2Min/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn2D_1/input2Min/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn2D_1/input2Max", [
			["float", "/TXOSCControlIn2D_1/input2Max/fixedValue", 1, 0, 1],
			["float", "/TXOSCControlIn2D_1/input2Max/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn2D_1/input2Max/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/input2Max/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/input2Max/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn2D_1/input2Max/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn2D_1/smoothTime", [
			["float", "/TXOSCControlIn2D_1/smoothTime/fixedValue", 0, 0, 2],
			["float", "/TXOSCControlIn2D_1/smoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn2D_1/smoothTime/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/smoothTime/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn2D_1/smoothTime/softMin", 0, 0, 10],
			["float", "/TXOSCControlIn2D_1/smoothTime/softMax", 2, 0, 10],
		]],
		[ "modParameterGroupBool", "/TXOSCControlIn2D_1/linearSmoothing", [
			["bool_int", "/TXOSCControlIn2D_1/linearSmoothing/fixedValue", 0],
			["bool_int", "/TXOSCControlIn2D_1/linearSmoothing/fixedModMix", 0],
			["bool_int", "/TXOSCControlIn2D_1/linearSmoothing/useExtMod", 0],
			["float", "/TXOSCControlIn2D_1/linearSmoothing/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXOSCControlIn2D_1 ---------- */
		"out1",
		"out2",
	];}
}


