// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_OSCControlIn : TXV_Module {
	classvar <defaultName = "TXV OSC ControlIn";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXOSCControlIn_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXOSCControlIn_1/active", [
			["bool_int", "/TXOSCControlIn_1/active/fixedValue", 1],
			["bool_int", "/TXOSCControlIn_1/active/fixedModMix", 0],
			["bool_int", "/TXOSCControlIn_1/active/useExtMod", 0],
			["float", "/TXOSCControlIn_1/active/extModValue", 0, 0, 1],
		]],
		["string", "/TXOSCControlIn_1/oscAddress", "/Example/text"],
		[ "modParameterGroupFloat", "/TXOSCControlIn_1/inputMin", [
			["float", "/TXOSCControlIn_1/inputMin/fixedValue", 0, 0, 1],
			["float", "/TXOSCControlIn_1/inputMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn_1/inputMin/useExtMod", 0],
			["float", "/TXOSCControlIn_1/inputMin/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn_1/inputMin/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn_1/inputMin/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn_1/inputMax", [
			["float", "/TXOSCControlIn_1/inputMax/fixedValue", 1, 0, 1],
			["float", "/TXOSCControlIn_1/inputMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn_1/inputMax/useExtMod", 0],
			["float", "/TXOSCControlIn_1/inputMax/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn_1/inputMax/softMin", 0, -1e+06, 1e+06],
			["float", "/TXOSCControlIn_1/inputMax/softMax", 1, -1e+06, 1e+06],
		]],
		[ "modParameterGroupFloat", "/TXOSCControlIn_1/smoothTime", [
			["float", "/TXOSCControlIn_1/smoothTime/fixedValue", 0, 0, 2],
			["float", "/TXOSCControlIn_1/smoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXOSCControlIn_1/smoothTime/useExtMod", 0],
			["float", "/TXOSCControlIn_1/smoothTime/extModValue", 0, 0, 1],
			["float", "/TXOSCControlIn_1/smoothTime/softMin", 0, 0, 10],
			["float", "/TXOSCControlIn_1/smoothTime/softMax", 2, 0, 10],
		]],
		[ "modParameterGroupBool", "/TXOSCControlIn_1/linearSmoothing", [
			["bool_int", "/TXOSCControlIn_1/linearSmoothing/fixedValue", 0],
			["bool_int", "/TXOSCControlIn_1/linearSmoothing/fixedModMix", 0],
			["bool_int", "/TXOSCControlIn_1/linearSmoothing/useExtMod", 0],
			["float", "/TXOSCControlIn_1/linearSmoothing/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXOSCControlIn_1 ---------- */
		"out",
	];}
}


