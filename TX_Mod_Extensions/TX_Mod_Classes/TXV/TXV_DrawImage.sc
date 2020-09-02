// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawImage : TXV_Module {
	classvar <defaultName = "TXV Image";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maskType/fixedValue":
	// , ["no mask", "use mask image", "feathered rectangle", "oval", "feathered oval", "blurred oval", "ring", "feathered ring", "diamond", "feathered diamond",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawImage_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["assetSlot/image", "assetSlot/image/maskImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		["extImageModuleSlot", "extImageModuleSlot/extMaskImageModule", 0],
		[ "modParameterGroupBool", "/TXDrawImage_1/drawActive", [
			["bool_int", "/TXDrawImage_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawImage_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawImage_1/drawActive/useExtMod", 0],
			["float", "/TXDrawImage_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawImage_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawImage_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawImage_1/customMaxHeight", 768, 1, 4096],
		["bool_int", "/TXDrawImage_1/useExternalSourceImage", 0],
		["bool_int", "/TXDrawImage_1/useExternalMaskImage", 0],
		[ "modParameterGroupInt", "/TXDrawImage_1/sourceImageScaleMode", [
			["int", "/TXDrawImage_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXDrawImage_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXDrawImage_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXDrawImage_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXDrawImage_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupInt", "/TXDrawImage_1/maskType", [
			["int", "/TXDrawImage_1/maskType/fixedValue", 0, 0, 9, ["no mask", "use mask image", "feathered rectangle", "oval", "feathered oval", "blurred oval", "ring", "feathered ring", "diamond", "feathered diamond",]],
			["float", "/TXDrawImage_1/maskType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/maskType/useExtMod", 0],
			["float", "/TXDrawImage_1/maskType/extModValue", 0, 0, 1],
			["int", "/TXDrawImage_1/maskType/softMin", 0, 0, 9],
			["int", "/TXDrawImage_1/maskType/softMax", 9, 0, 9],
		]],
		["bool_int", "/TXDrawImage_1/invertMask", 0],
		[ "modParameterGroupFloat", "/TXDrawImage_1/feathering", [
			["float", "/TXDrawImage_1/feathering/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawImage_1/feathering/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/feathering/useExtMod", 0],
			["float", "/TXDrawImage_1/feathering/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/feathering/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/feathering/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawImage_1/useSamplePosForDrawPos", 0],
		["bool_int", "/TXDrawImage_1/useSampleSizeForDrawSize", 0],
		["bool_int", "/TXDrawImage_1/constrainToEdges", 0],
		[ "modParameterGroupFloat", "/TXDrawImage_1/alpha", [
			["float", "/TXDrawImage_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawImage_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/alpha/useExtMod", 0],
			["float", "/TXDrawImage_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/alpha/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/samplePosX", [
			["float", "/TXDrawImage_1/samplePosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/samplePosX/useExtMod", 0],
			["float", "/TXDrawImage_1/samplePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosX/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/samplePosY", [
			["float", "/TXDrawImage_1/samplePosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/samplePosY/useExtMod", 0],
			["float", "/TXDrawImage_1/samplePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosY/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/samplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/sampleWidth", [
			["float", "/TXDrawImage_1/sampleWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawImage_1/sampleWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/sampleWidth/useExtMod", 0],
			["float", "/TXDrawImage_1/sampleWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/sampleWidth/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/sampleWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/sampleHeight", [
			["float", "/TXDrawImage_1/sampleHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawImage_1/sampleHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/sampleHeight/useExtMod", 0],
			["float", "/TXDrawImage_1/sampleHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/sampleHeight/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/sampleHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/numTiles", [
			["float", "/TXDrawImage_1/numTiles/fixedValue", 1, 1, 10],
			["float", "/TXDrawImage_1/numTiles/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/numTiles/useExtMod", 0],
			["float", "/TXDrawImage_1/numTiles/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/numTiles/softMin", 1, 1, 100],
			["float", "/TXDrawImage_1/numTiles/softMax", 10, 1, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawImage_1/mirrorTileX", [
			["bool_int", "/TXDrawImage_1/mirrorTileX/fixedValue", 1],
			["bool_int", "/TXDrawImage_1/mirrorTileX/fixedModMix", 0],
			["bool_int", "/TXDrawImage_1/mirrorTileX/useExtMod", 0],
			["float", "/TXDrawImage_1/mirrorTileX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawImage_1/mirrorTileY", [
			["bool_int", "/TXDrawImage_1/mirrorTileY/fixedValue", 1],
			["bool_int", "/TXDrawImage_1/mirrorTileY/fixedModMix", 0],
			["bool_int", "/TXDrawImage_1/mirrorTileY/useExtMod", 0],
			["float", "/TXDrawImage_1/mirrorTileY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/drawPosX", [
			["float", "/TXDrawImage_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawImage_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/drawPosX/useExtMod", 0],
			["float", "/TXDrawImage_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/drawPosY", [
			["float", "/TXDrawImage_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawImage_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/drawPosY/useExtMod", 0],
			["float", "/TXDrawImage_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/drawWidth", [
			["float", "/TXDrawImage_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawImage_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/drawWidth/useExtMod", 0],
			["float", "/TXDrawImage_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/drawHeight", [
			["float", "/TXDrawImage_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawImage_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/drawHeight/useExtMod", 0],
			["float", "/TXDrawImage_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/anchorX", [
			["float", "/TXDrawImage_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawImage_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/anchorX/useExtMod", 0],
			["float", "/TXDrawImage_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/anchorY", [
			["float", "/TXDrawImage_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawImage_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/anchorY/useExtMod", 0],
			["float", "/TXDrawImage_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawImage_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/rotate", [
			["float", "/TXDrawImage_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawImage_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/rotate/useExtMod", 0],
			["float", "/TXDrawImage_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawImage_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/rotateMultiply", [
			["float", "/TXDrawImage_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawImage_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawImage_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawImage_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/scaleX", [
			["float", "/TXDrawImage_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawImage_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/scaleX/useExtMod", 0],
			["float", "/TXDrawImage_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawImage_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawImage_1/scaleY", [
			["float", "/TXDrawImage_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawImage_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawImage_1/scaleY/useExtMod", 0],
			["float", "/TXDrawImage_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawImage_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawImage_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawImage_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawImage_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Sample Area", {arg parameterName;
				parameterName.containsi("samplePosX") ||
				parameterName.containsi("samplePosY") ||
				parameterName.containsi("sampleWidth") ||
				parameterName.containsi("sampleHeight")
				;}],
			["Tile", {arg parameterName;
				parameterName.containsi("Tile");}],
			["Mask", {arg parameterName;
				parameterName.containsi("Mask")
				|| parameterName.containsi("feathering")
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


