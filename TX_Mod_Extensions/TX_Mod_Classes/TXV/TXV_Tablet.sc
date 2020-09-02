// Copyright (C) 2014  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Tablet : TXV_Module {
	classvar <defaultName = "TXV Tablet";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXTablet_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXTablet_1/active", [
			["bool_int", "/TXTablet_1/active/fixedValue", 1],
			["bool_int", "/TXTablet_1/active/fixedModMix", 0],
			["bool_int", "/TXTablet_1/active/useExtMod", 0],
			["float", "/TXTablet_1/active/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXTablet_1 ---------- */
		"cursorInProximity",
		"eraserInProximity",
		"penInProximity",
		"positionX",
		"positionY",
		"pressure",
		"tiltX",
		"tiltY",
	];}
}


