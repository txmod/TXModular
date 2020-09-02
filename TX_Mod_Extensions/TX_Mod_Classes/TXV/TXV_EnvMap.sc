// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_EnvMap : TXV_Module {
	classvar <defaultName = "TXV EnvMap";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXEnvMap_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/envMapImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extEnvMapImageModule", 0],
		[ "modParameterGroupBool", "/TXEnvMap_1/drawActive", [
			["bool_int", "/TXEnvMap_1/drawActive/fixedValue", 1],
			["bool_int", "/TXEnvMap_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/drawActive/useExtMod", 0],
			["float", "/TXEnvMap_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXEnvMap_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXEnvMap_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXEnvMap_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXEnvMap_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXEnvMap_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXEnvMap_1/useExternalEnvMapImage", 0],
		[ "modParameterGroupBool", "/TXEnvMap_1/mirrorImageRight", [
			["bool_int", "/TXEnvMap_1/mirrorImageRight/fixedValue", 0],
			["bool_int", "/TXEnvMap_1/mirrorImageRight/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/mirrorImageRight/useExtMod", 0],
			["float", "/TXEnvMap_1/mirrorImageRight/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/environmentRotation", [
			["float", "/TXEnvMap_1/environmentRotation/fixedValue", 0.0, -360, 360],
			["float", "/TXEnvMap_1/environmentRotation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/environmentRotation/useExtMod", 0],
			["float", "/TXEnvMap_1/environmentRotation/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/environmentRotation/softMin", -360, -360, 360],
			["float", "/TXEnvMap_1/environmentRotation/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/blurTopBottom", [
			["float", "/TXEnvMap_1/blurTopBottom/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/blurTopBottom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/blurTopBottom/useExtMod", 0],
			["float", "/TXEnvMap_1/blurTopBottom/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/blurTopBottom/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/blurTopBottom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXEnvMap_1/drawLayersRule", [
			["int", "/TXEnvMap_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXEnvMap_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawLayersRule/useExtMod", 0],
			["float", "/TXEnvMap_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXEnvMap_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXEnvMap_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/renderContinuosly", [
			["bool_int", "/TXEnvMap_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXEnvMap_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/renderContinuosly/useExtMod", 0],
			["float", "/TXEnvMap_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/renderNow", [
			["bool_int", "/TXEnvMap_1/renderNow/fixedValue", 0],
			["bool_int", "/TXEnvMap_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/renderNow/useExtMod", 0],
			["float", "/TXEnvMap_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/clearBeforeRender", [
			["bool_int", "/TXEnvMap_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXEnvMap_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXEnvMap_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/clearNow", [
			["bool_int", "/TXEnvMap_1/clearNow/fixedValue", 0],
			["bool_int", "/TXEnvMap_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/clearNow/useExtMod", 0],
			["float", "/TXEnvMap_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/clearColorHue", [
			["float", "/TXEnvMap_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/clearColorHue/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/clearColorHueRotate", [
			["float", "/TXEnvMap_1/clearColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/clearColorHueRotate/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/clearColorSaturation", [
			["float", "/TXEnvMap_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXEnvMap_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/clearColorBrightness", [
			["float", "/TXEnvMap_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/clearColorAlpha", [
			["float", "/TXEnvMap_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXEnvMap_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/clearColorHSBAsRGB", [
			["bool_int", "/TXEnvMap_1/clearColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXEnvMap_1/clearColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/clearColorHSBAsRGB/useExtMod", 0],
			["float", "/TXEnvMap_1/clearColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/lightEffectMix", [
			["float", "/TXEnvMap_1/lightEffectMix/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/lightEffectMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/lightEffectMix/useExtMod", 0],
			["float", "/TXEnvMap_1/lightEffectMix/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightEffectMix/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/lightEffectMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/lightPosX", [
			["float", "/TXEnvMap_1/lightPosX/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/lightPosX/useExtMod", 0],
			["float", "/TXEnvMap_1/lightPosX/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightPosX/softMin", 0, -10, 10],
			["float", "/TXEnvMap_1/lightPosX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/lightPosY", [
			["float", "/TXEnvMap_1/lightPosY/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/lightPosY/useExtMod", 0],
			["float", "/TXEnvMap_1/lightPosY/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightPosY/softMin", 0, -10, 10],
			["float", "/TXEnvMap_1/lightPosY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/lightPosZ", [
			["float", "/TXEnvMap_1/lightPosZ/fixedValue", 0, -1, 0],
			["float", "/TXEnvMap_1/lightPosZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/lightPosZ/useExtMod", 0],
			["float", "/TXEnvMap_1/lightPosZ/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/lightPosZ/softMin", -1, -10, 10],
			["float", "/TXEnvMap_1/lightPosZ/softMax", 0, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/baseColorHue", [
			["float", "/TXEnvMap_1/baseColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/baseColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/baseColorHue/useExtMod", 0],
			["float", "/TXEnvMap_1/baseColorHue/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorHue/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/baseColorHueRotate", [
			["float", "/TXEnvMap_1/baseColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/baseColorHueRotate/useExtMod", 0],
			["float", "/TXEnvMap_1/baseColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/baseColorSaturation", [
			["float", "/TXEnvMap_1/baseColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/baseColorSaturation/useExtMod", 0],
			["float", "/TXEnvMap_1/baseColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorSaturation/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/baseColorBrightness", [
			["float", "/TXEnvMap_1/baseColorBrightness/fixedValue", 0.4, 0, 1],
			["float", "/TXEnvMap_1/baseColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/baseColorBrightness/useExtMod", 0],
			["float", "/TXEnvMap_1/baseColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorBrightness/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/baseColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXEnvMap_1/baseColorHSBAsRGB", [
			["bool_int", "/TXEnvMap_1/baseColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXEnvMap_1/baseColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXEnvMap_1/baseColorHSBAsRGB/useExtMod", 0],
			["float", "/TXEnvMap_1/baseColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/baseEnvColorMix", [
			["float", "/TXEnvMap_1/baseEnvColorMix/fixedValue", 0.75, 0, 1],
			["float", "/TXEnvMap_1/baseEnvColorMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/baseEnvColorMix/useExtMod", 0],
			["float", "/TXEnvMap_1/baseEnvColorMix/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/baseEnvColorMix/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/baseEnvColorMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/drawAlpha", [
			["float", "/TXEnvMap_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXEnvMap_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawAlpha/useExtMod", 0],
			["float", "/TXEnvMap_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/drawPosX", [
			["float", "/TXEnvMap_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawPosX/useExtMod", 0],
			["float", "/TXEnvMap_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/drawPosY", [
			["float", "/TXEnvMap_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawPosY/useExtMod", 0],
			["float", "/TXEnvMap_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/drawWidth", [
			["float", "/TXEnvMap_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXEnvMap_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawWidth/useExtMod", 0],
			["float", "/TXEnvMap_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/drawHeight", [
			["float", "/TXEnvMap_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXEnvMap_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/drawHeight/useExtMod", 0],
			["float", "/TXEnvMap_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/anchorX", [
			["float", "/TXEnvMap_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/anchorX/useExtMod", 0],
			["float", "/TXEnvMap_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/anchorY", [
			["float", "/TXEnvMap_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXEnvMap_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/anchorY/useExtMod", 0],
			["float", "/TXEnvMap_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXEnvMap_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/rotate", [
			["float", "/TXEnvMap_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXEnvMap_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/rotate/useExtMod", 0],
			["float", "/TXEnvMap_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/rotate/softMin", -360, -360, 360],
			["float", "/TXEnvMap_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/rotateMultiply", [
			["float", "/TXEnvMap_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXEnvMap_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/rotateMultiply/useExtMod", 0],
			["float", "/TXEnvMap_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXEnvMap_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/scaleX", [
			["float", "/TXEnvMap_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXEnvMap_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/scaleX/useExtMod", 0],
			["float", "/TXEnvMap_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXEnvMap_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXEnvMap_1/scaleY", [
			["float", "/TXEnvMap_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXEnvMap_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXEnvMap_1/scaleY/useExtMod", 0],
			["float", "/TXEnvMap_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXEnvMap_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXEnvMap_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXEnvMap_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXEnvMap_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Light Position", {arg argName; argName.contains("lightPos");}],
			["Base Color", {arg argName; argName.contains("baseColor");}],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule", "maxDepthRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
					"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Environment Image";
	}

}


