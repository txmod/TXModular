// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_TileMask : TXV_Module {
	classvar <defaultName = "TXV Tile & Mask";
	classvar <>arrInstances;

	// add text array to  ..."sourceImageScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maskType/fixedValue":
	// , ["no mask", "use mask image", "feathered rectangle", "oval", "feathered oval", "blurred oval", "ring", "feathered ring", "diamond", "feathered diamond",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXTileMask_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["assetSlot/image", "assetSlot/image/maskImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		["extImageModuleSlot", "extImageModuleSlot/extMaskImageModule", 0],
		[ "modParameterGroupBool", "/TXTileMask_1/drawActive", [
			["bool_int", "/TXTileMask_1/drawActive/fixedValue", 1],
			["bool_int", "/TXTileMask_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/drawActive/useExtMod", 0],
			["float", "/TXTileMask_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXTileMask_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		// ["int", "/TXTileMask_1/maxDepthRule", 0, 0, 4], // hidden
		["int", "/TXTileMask_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXTileMask_1/customMaxHeight", 768, 1, 4096],
		// ["int", "/TXTileMask_1/customMaxDepth", 1024, 1, 4096], // hidden
		["bool_int", "/TXTileMask_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXTileMask_1/sourceImageScaleMode", [
			["int", "/TXTileMask_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXTileMask_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXTileMask_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXTileMask_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXTileMask_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXTileMask_1/renderContinuosly", [
			["bool_int", "/TXTileMask_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXTileMask_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/renderContinuosly/useExtMod", 0],
			["float", "/TXTileMask_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXTileMask_1/renderNow", [
			["bool_int", "/TXTileMask_1/renderNow/fixedValue", 0],
			["bool_int", "/TXTileMask_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/renderNow/useExtMod", 0],
			["float", "/TXTileMask_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXTileMask_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXTileMask_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXTileMask_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXTileMask_1/clearBeforeRender", [
			["bool_int", "/TXTileMask_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXTileMask_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXTileMask_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXTileMask_1/clearNow", [
			["bool_int", "/TXTileMask_1/clearNow/fixedValue", 0],
			["bool_int", "/TXTileMask_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/clearNow/useExtMod", 0],
			["float", "/TXTileMask_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/clearColorHue", [
			["float", "/TXTileMask_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/clearColorHue/useExtMod", 0],
			["float", "/TXTileMask_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/clearColorSaturation", [
			["float", "/TXTileMask_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXTileMask_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXTileMask_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/clearColorBrightness", [
			["float", "/TXTileMask_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXTileMask_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/clearColorAlpha", [
			["float", "/TXTileMask_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXTileMask_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXTileMask_1/clearColorHSBAsRGB", 0],
		["bool_int", "/TXTileMask_1/useExternalMaskImage", 0],
		[ "modParameterGroupInt", "/TXTileMask_1/maskType", [
			["int", "/TXTileMask_1/maskType/fixedValue", 0, 0, 9, ["no mask", "use mask image", "feathered rectangle", "oval", "feathered oval", "blurred oval", "ring", "feathered ring", "diamond", "feathered diamond",]],
			["float", "/TXTileMask_1/maskType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/maskType/useExtMod", 0],
			["float", "/TXTileMask_1/maskType/extModValue", 0, 0, 1],
			["int", "/TXTileMask_1/maskType/softMin", 0, 0, 9],
			["int", "/TXTileMask_1/maskType/softMax", 9, 0, 9],
		]],
		["bool_int", "/TXTileMask_1/invertMask", 0],
		[ "modParameterGroupFloat", "/TXTileMask_1/feathering", [
			["float", "/TXTileMask_1/feathering/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/feathering/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/feathering/useExtMod", 0],
			["float", "/TXTileMask_1/feathering/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/feathering/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/feathering/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXTileMask_1/useSamplePosForRenderPos", 0],
		["bool_int", "/TXTileMask_1/useSampleSizeForRenderSize", 0],
		["bool_int", "/TXTileMask_1/constrainToEdges", 0],
		[ "modParameterGroupFloat", "/TXTileMask_1/alpha", [
			["float", "/TXTileMask_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/alpha/useExtMod", 0],
			["float", "/TXTileMask_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/alpha/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/samplePosX", [
			["float", "/TXTileMask_1/samplePosX/fixedValue", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/samplePosX/useExtMod", 0],
			["float", "/TXTileMask_1/samplePosX/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosX/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/samplePosY", [
			["float", "/TXTileMask_1/samplePosY/fixedValue", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/samplePosY/useExtMod", 0],
			["float", "/TXTileMask_1/samplePosY/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosY/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/samplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/sampleWidth", [
			["float", "/TXTileMask_1/sampleWidth/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/sampleWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/sampleWidth/useExtMod", 0],
			["float", "/TXTileMask_1/sampleWidth/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/sampleWidth/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/sampleWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/sampleHeight", [
			["float", "/TXTileMask_1/sampleHeight/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/sampleHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/sampleHeight/useExtMod", 0],
			["float", "/TXTileMask_1/sampleHeight/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/sampleHeight/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/sampleHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/numTiles", [
			["float", "/TXTileMask_1/numTiles/fixedValue", 1, 1, 10],
			["float", "/TXTileMask_1/numTiles/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/numTiles/useExtMod", 0],
			["float", "/TXTileMask_1/numTiles/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/numTiles/softMin", 1, 1, 100],
			["float", "/TXTileMask_1/numTiles/softMax", 10, 1, 100],
		]],
		[ "modParameterGroupBool", "/TXTileMask_1/mirrorTileX", [
			["bool_int", "/TXTileMask_1/mirrorTileX/fixedValue", 1],
			["bool_int", "/TXTileMask_1/mirrorTileX/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/mirrorTileX/useExtMod", 0],
			["float", "/TXTileMask_1/mirrorTileX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXTileMask_1/mirrorTileY", [
			["bool_int", "/TXTileMask_1/mirrorTileY/fixedValue", 1],
			["bool_int", "/TXTileMask_1/mirrorTileY/fixedModMix", 0],
			["bool_int", "/TXTileMask_1/mirrorTileY/useExtMod", 0],
			["float", "/TXTileMask_1/mirrorTileY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/drawAlpha", [
			["float", "/TXTileMask_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/drawAlpha/useExtMod", 0],
			["float", "/TXTileMask_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/drawPosX", [
			["float", "/TXTileMask_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/drawPosX/useExtMod", 0],
			["float", "/TXTileMask_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/drawPosY", [
			["float", "/TXTileMask_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/drawPosY/useExtMod", 0],
			["float", "/TXTileMask_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/drawWidth", [
			["float", "/TXTileMask_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/drawWidth/useExtMod", 0],
			["float", "/TXTileMask_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/drawHeight", [
			["float", "/TXTileMask_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXTileMask_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/drawHeight/useExtMod", 0],
			["float", "/TXTileMask_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/anchorX", [
			["float", "/TXTileMask_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/anchorX/useExtMod", 0],
			["float", "/TXTileMask_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/anchorY", [
			["float", "/TXTileMask_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXTileMask_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/anchorY/useExtMod", 0],
			["float", "/TXTileMask_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXTileMask_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/rotate", [
			["float", "/TXTileMask_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXTileMask_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/rotate/useExtMod", 0],
			["float", "/TXTileMask_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/rotate/softMin", -360, -360, 360],
			["float", "/TXTileMask_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/rotateMultiply", [
			["float", "/TXTileMask_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXTileMask_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/rotateMultiply/useExtMod", 0],
			["float", "/TXTileMask_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXTileMask_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/scaleX", [
			["float", "/TXTileMask_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXTileMask_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/scaleX/useExtMod", 0],
			["float", "/TXTileMask_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXTileMask_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXTileMask_1/scaleY", [
			["float", "/TXTileMask_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXTileMask_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXTileMask_1/scaleY/useExtMod", 0],
			["float", "/TXTileMask_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXTileMask_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXTileMask_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXTileMask_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXTileMask_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Sample Area", {arg parameterName;
				parameterName.containsi("samplePosX") ||
				parameterName.containsi("samplePosY") ||
				parameterName.containsi("sampleWidth") ||
				parameterName.containsi("sampleHeight") ||
				parameterName.containsi("useSamplePosForRenderPos") ||
				parameterName.containsi("useSampleSizeForRenderSize")
				;}],
			["Tile", {arg parameterName;
				parameterName.containsi("Tile");}],
			["Mask", {arg parameterName;
				parameterName.containsi("Mask")
				|| parameterName.containsi("feather")
				;}],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha",
				{arg parameterName;
					var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
						"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
						"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
						"drawAlpha", "drawWidth", "drawHeight", ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale",
				{arg parameterName;
					var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate",
				{arg parameterName;
					var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
						"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Source Image";
	}

}


