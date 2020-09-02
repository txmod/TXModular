// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

// NOTE - THIS MODULE IS SLOW - USE LUTFast instead

TXV_LUT : TXV_Module {
	classvar <defaultName = "TXV LUTSlow";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXLUT_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["assetSlot/cube", "assetSlot/cube/cubeAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXLUT_1/drawActive", [
			["bool_int", "/TXLUT_1/drawActive/fixedValue", 1],
			["bool_int", "/TXLUT_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXLUT_1/drawActive/useExtMod", 0],
			["float", "/TXLUT_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXLUT_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXLUT_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXLUT_1/customMaxHeight", 768, 1, 4096],
		["bool_int", "/TXLUT_1/useExternalSourceImage", 0],
		[ "modParameterGroupBool", "/TXLUT_1/renderContinuosly", [
			["bool_int", "/TXLUT_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXLUT_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXLUT_1/renderContinuosly/useExtMod", 0],
			["float", "/TXLUT_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLUT_1/renderNow", [
			["bool_int", "/TXLUT_1/renderNow/fixedValue", 0],
			["bool_int", "/TXLUT_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXLUT_1/renderNow/useExtMod", 0],
			["float", "/TXLUT_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/drawAlpha", [
			["float", "/TXLUT_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXLUT_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/drawAlpha/useExtMod", 0],
			["float", "/TXLUT_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXLUT_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/drawPosX", [
			["float", "/TXLUT_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXLUT_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/drawPosX/useExtMod", 0],
			["float", "/TXLUT_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXLUT_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/drawPosY", [
			["float", "/TXLUT_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXLUT_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/drawPosY/useExtMod", 0],
			["float", "/TXLUT_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXLUT_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/drawWidth", [
			["float", "/TXLUT_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXLUT_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/drawWidth/useExtMod", 0],
			["float", "/TXLUT_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXLUT_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/drawHeight", [
			["float", "/TXLUT_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXLUT_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/drawHeight/useExtMod", 0],
			["float", "/TXLUT_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXLUT_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/anchorX", [
			["float", "/TXLUT_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXLUT_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/anchorX/useExtMod", 0],
			["float", "/TXLUT_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXLUT_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/anchorY", [
			["float", "/TXLUT_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXLUT_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/anchorY/useExtMod", 0],
			["float", "/TXLUT_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXLUT_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/rotate", [
			["float", "/TXLUT_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXLUT_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/rotate/useExtMod", 0],
			["float", "/TXLUT_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/rotate/softMin", -360, -360, 360],
			["float", "/TXLUT_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/rotateMultiply", [
			["float", "/TXLUT_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXLUT_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/rotateMultiply/useExtMod", 0],
			["float", "/TXLUT_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXLUT_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/scaleX", [
			["float", "/TXLUT_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXLUT_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/scaleX/useExtMod", 0],
			["float", "/TXLUT_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXLUT_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXLUT_1/scaleY", [
			["float", "/TXLUT_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXLUT_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLUT_1/scaleY/useExtMod", 0],
			["float", "/TXLUT_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXLUT_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXLUT_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXLUT_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXLUT_1 ---------- */
		// empty
	];}


	getDefaultParamSectionName {  // override
		^"Source Image, Cube, Render";
	}

}


