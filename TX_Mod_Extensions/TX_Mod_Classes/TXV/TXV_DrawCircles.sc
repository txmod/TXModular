// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawCircles : TXV_Module {
	classvar <defaultName = "TXV Circles";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawCircles_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawCircles_1/drawActive", [
			["bool_int", "/TXDrawCircles_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawCircles_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawCircles_1/drawActive/useExtMod", 0],
			["float", "/TXDrawCircles_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawCircles_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawCircles_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawCircles_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/size", [
			["float", "/TXDrawCircles_1/size/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawCircles_1/size/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/size/useExtMod", 0],
			["float", "/TXDrawCircles_1/size/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/size/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/size/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawCircles_1/numCircles", [
			["int", "/TXDrawCircles_1/numCircles/fixedValue", 4, 1, 20],
			["float", "/TXDrawCircles_1/numCircles/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/numCircles/useExtMod", 0],
			["float", "/TXDrawCircles_1/numCircles/extModValue", 0, 0, 1],
			["int", "/TXDrawCircles_1/numCircles/softMin", 1, 1, 1000],
			["int", "/TXDrawCircles_1/numCircles/softMax", 20, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/positionX", [
			["float", "/TXDrawCircles_1/positionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCircles_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/positionX/useExtMod", 0],
			["float", "/TXDrawCircles_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/positionX/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/positionX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/positionY", [
			["float", "/TXDrawCircles_1/positionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCircles_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/positionY/useExtMod", 0],
			["float", "/TXDrawCircles_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/positionY/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/positionY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/colorH", [
			["float", "/TXDrawCircles_1/colorH/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCircles_1/colorH/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/colorH/useExtMod", 0],
			["float", "/TXDrawCircles_1/colorH/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorH/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorH/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/colorS", [
			["float", "/TXDrawCircles_1/colorS/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCircles_1/colorS/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/colorS/useExtMod", 0],
			["float", "/TXDrawCircles_1/colorS/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorS/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorS/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/colorB", [
			["float", "/TXDrawCircles_1/colorB/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCircles_1/colorB/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/colorB/useExtMod", 0],
			["float", "/TXDrawCircles_1/colorB/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorB/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorB/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCircles_1/colorA", [
			["float", "/TXDrawCircles_1/colorA/fixedValue", 1, 0, 1],
			["float", "/TXDrawCircles_1/colorA/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCircles_1/colorA/useExtMod", 0],
			["float", "/TXDrawCircles_1/colorA/extModValue", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorA/softMin", 0, 0, 1],
			["float", "/TXDrawCircles_1/colorA/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawCircles_1 ---------- */
		// empty
	];}

}


