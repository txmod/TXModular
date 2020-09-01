// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawText3D : TXV_Module {
	classvar <defaultName = "TXV Text3D";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawText3D_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/font", "assetSlot/font/fontAsset", 0],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawActive", [
			["bool_int", "/TXDrawText3D_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawText3D_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawActive/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawText3D_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawText3D_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawText3D_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDrawText3D_1/customMaxDepth", 1024, 1, 4096],
		["string", "/TXDrawText3D_1/textString", "Text 0123456789"],
		[ "modParameterGroupInt", "/TXDrawText3D_1/fontsize", [
			["int", "/TXDrawText3D_1/fontsize/fixedValue", 24, 1, 200],
			["float", "/TXDrawText3D_1/fontsize/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/fontsize/useExtMod", 0],
			["float", "/TXDrawText3D_1/fontsize/extModValue", 0, 0, 1],
			["int", "/TXDrawText3D_1/fontsize/softMin", 1, 1, 1000],
			["int", "/TXDrawText3D_1/fontsize/softMax", 200, 1, 1000],
		]],
		["bool_int", "/TXDrawText3D_1/scaleFontToWidth_1024", 1],
		[ "modParameterGroupInt", "/TXDrawText3D_1/depth", [
			["int", "/TXDrawText3D_1/depth/fixedValue", 24, 1, 1000],
			["float", "/TXDrawText3D_1/depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/depth/useExtMod", 0],
			["float", "/TXDrawText3D_1/depth/extModValue", 0, 0, 1],
			["int", "/TXDrawText3D_1/depth/softMin", 1, 1, 1000],
			["int", "/TXDrawText3D_1/depth/softMax", 1000, 1, 1000],
		]],
		[ "modParameterGroupInt", "/TXDrawText3D_1/depthResolution", [
			["int", "/TXDrawText3D_1/depthResolution/fixedValue", 1, 1, 1000],
			["float", "/TXDrawText3D_1/depthResolution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/depthResolution/useExtMod", 0],
			["float", "/TXDrawText3D_1/depthResolution/extModValue", 0, 0, 1],
			["int", "/TXDrawText3D_1/depthResolution/softMin", 1, 1, 1000],
			["int", "/TXDrawText3D_1/depthResolution/softMax", 1000, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/fontSimplify", [
			["float", "/TXDrawText3D_1/fontSimplify/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawText3D_1/fontSimplify/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/fontSimplify/useExtMod", 0],
			["float", "/TXDrawText3D_1/fontSimplify/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/fontSimplify/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/fontSimplify/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/spaceSize", [
			["float", "/TXDrawText3D_1/spaceSize/fixedValue", 5, 0.01, 50],
			["float", "/TXDrawText3D_1/spaceSize/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/spaceSize/useExtMod", 0],
			["float", "/TXDrawText3D_1/spaceSize/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/spaceSize/softMin", 0.01, 0.01, 1000],
			["float", "/TXDrawText3D_1/spaceSize/softMax", 50, 0.01, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/letterSpacing", [
			["float", "/TXDrawText3D_1/letterSpacing/fixedValue", 1, 0.01, 10],
			["float", "/TXDrawText3D_1/letterSpacing/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/letterSpacing/useExtMod", 0],
			["float", "/TXDrawText3D_1/letterSpacing/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/letterSpacing/softMin", 0.01, 0.01, 100],
			["float", "/TXDrawText3D_1/letterSpacing/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterFrontSolid", [
			["bool_int", "/TXDrawText3D_1/drawLetterFrontSolid/fixedValue", 1],
			["bool_int", "/TXDrawText3D_1/drawLetterFrontSolid/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterFrontSolid/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterFrontSolid/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterFrontWireframe", [
			["bool_int", "/TXDrawText3D_1/drawLetterFrontWireframe/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterFrontWireframe/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterFrontWireframe/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterFrontWireframe/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterBackSolid", [
			["bool_int", "/TXDrawText3D_1/drawLetterBackSolid/fixedValue", 1],
			["bool_int", "/TXDrawText3D_1/drawLetterBackSolid/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterBackSolid/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterBackSolid/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterBackWireframe", [
			["bool_int", "/TXDrawText3D_1/drawLetterBackWireframe/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterBackWireframe/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterBackWireframe/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterBackWireframe/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterSidesSolid", [
			["bool_int", "/TXDrawText3D_1/drawLetterSidesSolid/fixedValue", 1],
			["bool_int", "/TXDrawText3D_1/drawLetterSidesSolid/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterSidesSolid/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterSidesSolid/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/drawLetterSidesWireframe", [
			["bool_int", "/TXDrawText3D_1/drawLetterSidesWireframe/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterSidesWireframe/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/drawLetterSidesWireframe/useExtMod", 0],
			["float", "/TXDrawText3D_1/drawLetterSidesWireframe/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXDrawText3D_1/useColorFrontForColorBack", 0],
		["bool_int", "/TXDrawText3D_1/useColorFrontForColorSides", 0],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorFrontHue", [
			["float", "/TXDrawText3D_1/colorFrontHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorFrontHue/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorFrontHue/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHue/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorFrontHueRotate", [
			["float", "/TXDrawText3D_1/colorFrontHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorFrontHueRotate/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorFrontHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorFrontSaturation", [
			["float", "/TXDrawText3D_1/colorFrontSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorFrontSaturation/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorFrontSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorFrontBrightness", [
			["float", "/TXDrawText3D_1/colorFrontBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorFrontBrightness/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorFrontBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorFrontBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/colorFrontHSBAsRGB", [
			["bool_int", "/TXDrawText3D_1/colorFrontHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/colorFrontHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/colorFrontHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorFrontHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorBackHue", [
			["float", "/TXDrawText3D_1/colorBackHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorBackHue/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorBackHue/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHue/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorBackHueRotate", [
			["float", "/TXDrawText3D_1/colorBackHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorBackHueRotate/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorBackHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorBackSaturation", [
			["float", "/TXDrawText3D_1/colorBackSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawText3D_1/colorBackSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorBackSaturation/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorBackSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorBackBrightness", [
			["float", "/TXDrawText3D_1/colorBackBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorBackBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorBackBrightness/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorBackBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorBackBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/colorBackHSBAsRGB", [
			["bool_int", "/TXDrawText3D_1/colorBackHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/colorBackHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/colorBackHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorBackHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorSidesHue", [
			["float", "/TXDrawText3D_1/colorSidesHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorSidesHue/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorSidesHue/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHue/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorSidesHueRotate", [
			["float", "/TXDrawText3D_1/colorSidesHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorSidesHueRotate/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorSidesHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorSidesSaturation", [
			["float", "/TXDrawText3D_1/colorSidesSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorSidesSaturation/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorSidesSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/colorSidesBrightness", [
			["float", "/TXDrawText3D_1/colorSidesBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/colorSidesBrightness/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorSidesBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/colorSidesBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawText3D_1/colorSidesHSBAsRGB", [
			["bool_int", "/TXDrawText3D_1/colorSidesHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawText3D_1/colorSidesHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawText3D_1/colorSidesHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawText3D_1/colorSidesHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/positionX", [
			["float", "/TXDrawText3D_1/positionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/positionX/useExtMod", 0],
			["float", "/TXDrawText3D_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/positionX/softMin", 0, -20, 20],
			["float", "/TXDrawText3D_1/positionX/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/positionY", [
			["float", "/TXDrawText3D_1/positionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/positionY/useExtMod", 0],
			["float", "/TXDrawText3D_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/positionY/softMin", 0, -20, 20],
			["float", "/TXDrawText3D_1/positionY/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/positionZ", [
			["float", "/TXDrawText3D_1/positionZ/fixedValue", 0, -1, 0],
			["float", "/TXDrawText3D_1/positionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/positionZ/useExtMod", 0],
			["float", "/TXDrawText3D_1/positionZ/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/positionZ/softMin", -1, -20, 20],
			["float", "/TXDrawText3D_1/positionZ/softMax", 0, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/rotateX", [
			["float", "/TXDrawText3D_1/rotateX/fixedValue", 0, -360, 360],
			["float", "/TXDrawText3D_1/rotateX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/rotateX/useExtMod", 0],
			["float", "/TXDrawText3D_1/rotateX/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/rotateX/softMin", -360, -360, 360],
			["float", "/TXDrawText3D_1/rotateX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/rotateY", [
			["float", "/TXDrawText3D_1/rotateY/fixedValue", 0, -360, 360],
			["float", "/TXDrawText3D_1/rotateY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/rotateY/useExtMod", 0],
			["float", "/TXDrawText3D_1/rotateY/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/rotateY/softMin", -360, -360, 360],
			["float", "/TXDrawText3D_1/rotateY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/rotateZ", [
			["float", "/TXDrawText3D_1/rotateZ/fixedValue", 0, -360, 360],
			["float", "/TXDrawText3D_1/rotateZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/rotateZ/useExtMod", 0],
			["float", "/TXDrawText3D_1/rotateZ/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/rotateZ/softMin", -360, -360, 360],
			["float", "/TXDrawText3D_1/rotateZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/rotateMultiply", [
			["float", "/TXDrawText3D_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawText3D_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawText3D_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawText3D_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/anchorX", [
			["float", "/TXDrawText3D_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/anchorX/useExtMod", 0],
			["float", "/TXDrawText3D_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/anchorY", [
			["float", "/TXDrawText3D_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawText3D_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/anchorY/useExtMod", 0],
			["float", "/TXDrawText3D_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/anchorZ", [
			["float", "/TXDrawText3D_1/anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/anchorZ/useExtMod", 0],
			["float", "/TXDrawText3D_1/anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawText3D_1/anchorZ/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/scaleX", [
			["float", "/TXDrawText3D_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawText3D_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/scaleX/useExtMod", 0],
			["float", "/TXDrawText3D_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawText3D_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/scaleY", [
			["float", "/TXDrawText3D_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawText3D_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/scaleY/useExtMod", 0],
			["float", "/TXDrawText3D_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawText3D_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawText3D_1/scaleZ", [
			["float", "/TXDrawText3D_1/scaleZ/fixedValue", 1, 0, 3],
			["float", "/TXDrawText3D_1/scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawText3D_1/scaleZ/useExtMod", 0],
			["float", "/TXDrawText3D_1/scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawText3D_1/scaleZ/softMin", 0, -10, 10],
			["float", "/TXDrawText3D_1/scaleZ/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawText3D_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDrawText3D_1/useScaleXForScaleZ", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawText3D_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Face Options", {arg parameterName;
				parameterName.containsi("drawLetter")
				;}],
			["Colors$Color Front", {arg parameterName;
				var arrNames = ["colorFrontHue", "colorFrontHueRotate", "colorFrontSaturation",
					"colorFrontBrightness", "colorFrontAlpha", "colorFrontHSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Colors$Color Back", {arg parameterName;
				var arrNames = ["useColorFrontForColorBack", "colorBackHue", "colorBackHueRotate", "colorBackSaturation",
					"colorBackBrightness", "colorBackAlpha", "colorBackHSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Colors$Color Sides", {arg parameterName;
				var arrNames = ["useColorFrontForColorSides", "colorSidesHue", "colorSidesHueRotate", "colorSidesSaturation",
					"colorSidesBrightness", "colorSidesAlpha", "colorSidesHSBAsRGB", ]
				;
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Active & Position", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
					"drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ", "useSamplePosForDrawPos",];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Anchor & Rotate", {arg parameterName;
				var arrNames = ["anchorX", "anchorY", "anchorZ", "rotate", "rotateX", "rotateY", "rotateZ",
					"rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Text Controls";
	}

}


