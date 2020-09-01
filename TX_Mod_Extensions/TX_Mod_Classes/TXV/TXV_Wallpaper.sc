// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Wallpaper : TXV_Module {
	classvar <defaultName = "TXV Wallpaper";
	classvar <>arrInstances;

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."wallpaperGroup/fixedValue":
	// , ["P4, Square, 90 deg. rotation", "P4M, Square, 90 deg. rotation, 45 degree Mirroring", "P4G, Square, 90 deg. rotation, Mirroring", "P3, Hexagonal, 120 deg. rotation", "P3M1, Hexagonal, 120 deg. rotation, Mirroring", "P31M, Hexagonal, 120 deg. rotation, Mirroring, Off-centre rotation", "P6, Hexagonal, 60 deg. rotation", 	"P6M, Hexagonal, 60 deg. rotation, Mirroring",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXWallpaper_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXWallpaper_1/drawActive", [
			["bool_int", "/TXWallpaper_1/drawActive/fixedValue", 1],
			["bool_int", "/TXWallpaper_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXWallpaper_1/drawActive/useExtMod", 0],
			["float", "/TXWallpaper_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXWallpaper_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		// ["int", "/TXWallpaper_1/maxDepthRule", 0, 0, 4],  // hidden
		["int", "/TXWallpaper_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXWallpaper_1/customMaxHeight", 768, 1, 4096],
		// ["int", "/TXWallpaper_1/customMaxDepth", 1024, 1, 4096],  // hidden
		["bool_int", "/TXWallpaper_1/useExternalSourceImage", 0],
		[ "modParameterGroupBool", "/TXWallpaper_1/renderContinuosly", [
			["bool_int", "/TXWallpaper_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXWallpaper_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXWallpaper_1/renderContinuosly/useExtMod", 0],
			["float", "/TXWallpaper_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXWallpaper_1/renderNow", [
			["bool_int", "/TXWallpaper_1/renderNow/fixedValue", 0],
			["bool_int", "/TXWallpaper_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXWallpaper_1/renderNow/useExtMod", 0],
			["float", "/TXWallpaper_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXWallpaper_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXWallpaper_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXWallpaper_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXWallpaper_1/wallpaperGroup", [
			["int", "/TXWallpaper_1/wallpaperGroup/fixedValue", 0, 0, 7, ["P4, Square, 90 deg. rotation", "P4M, Square, 90 deg. rotation, 45 degree Mirroring", "P4G, Square, 90 deg. rotation, Mirroring", "P3, Hexagonal, 120 deg. rotation", "P3M1, Hexagonal, 120 deg. rotation, Mirroring", "P31M, Hexagonal, 120 deg. rotation, Mirroring, Off-centre rotation", "P6, Hexagonal, 60 deg. rotation", 	"P6M, Hexagonal, 60 deg. rotation, Mirroring",]],
			["float", "/TXWallpaper_1/wallpaperGroup/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/wallpaperGroup/useExtMod", 0],
			["float", "/TXWallpaper_1/wallpaperGroup/extModValue", 0, 0, 1],
			["int", "/TXWallpaper_1/wallpaperGroup/softMin", 0, 0, 7],
			["int", "/TXWallpaper_1/wallpaperGroup/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/sourceZoomX", [
			["float", "/TXWallpaper_1/sourceZoomX/fixedValue", 1, 1, 3],
			["float", "/TXWallpaper_1/sourceZoomX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/sourceZoomX/useExtMod", 0],
			["float", "/TXWallpaper_1/sourceZoomX/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/sourceZoomX/softMin", 1, 0.001, 10],
			["float", "/TXWallpaper_1/sourceZoomX/softMax", 3, 0.001, 10],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/sourceZoomY", [
			["float", "/TXWallpaper_1/sourceZoomY/fixedValue", 1, 1, 3],
			["float", "/TXWallpaper_1/sourceZoomY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/sourceZoomY/useExtMod", 0],
			["float", "/TXWallpaper_1/sourceZoomY/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/sourceZoomY/softMin", 1, 0.001, 10],
			["float", "/TXWallpaper_1/sourceZoomY/softMax", 3, 0.001, 10],
		]],
		["bool_int", "/TXWallpaper_1/useSourceZoomXForZoomY", 0],
		[ "modParameterGroupFloat", "/TXWallpaper_1/sourceShiftX", [
			["float", "/TXWallpaper_1/sourceShiftX/fixedValue", 0, -1, 1],
			["float", "/TXWallpaper_1/sourceShiftX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/sourceShiftX/useExtMod", 0],
			["float", "/TXWallpaper_1/sourceShiftX/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/sourceShiftX/softMin", -1, -1, 1],
			["float", "/TXWallpaper_1/sourceShiftX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/sourceShiftY", [
			["float", "/TXWallpaper_1/sourceShiftY/fixedValue", 0, -1, 1],
			["float", "/TXWallpaper_1/sourceShiftY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/sourceShiftY/useExtMod", 0],
			["float", "/TXWallpaper_1/sourceShiftY/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/sourceShiftY/softMin", -1, -1, 1],
			["float", "/TXWallpaper_1/sourceShiftY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/sourceRotate", [
			["float", "/TXWallpaper_1/sourceRotate/fixedValue", 0, -360, 360],
			["float", "/TXWallpaper_1/sourceRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/sourceRotate/useExtMod", 0],
			["float", "/TXWallpaper_1/sourceRotate/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/sourceRotate/softMin", -360, -360, 360],
			["float", "/TXWallpaper_1/sourceRotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/tileSize", [
			["float", "/TXWallpaper_1/tileSize/fixedValue", 0.1, 0.001, 1],
			["float", "/TXWallpaper_1/tileSize/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/tileSize/useExtMod", 0],
			["float", "/TXWallpaper_1/tileSize/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/tileSize/softMin", 0.001, 0.001, 1],
			["float", "/TXWallpaper_1/tileSize/softMax", 1, 0.001, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/tileAngle", [
			["float", "/TXWallpaper_1/tileAngle/fixedValue", 0, -360, 360],
			["float", "/TXWallpaper_1/tileAngle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/tileAngle/useExtMod", 0],
			["float", "/TXWallpaper_1/tileAngle/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/tileAngle/softMin", -360, -360, 360],
			["float", "/TXWallpaper_1/tileAngle/softMax", 360, -360, 360],
		]],
		["float", "/TXWallpaper_1/alphaThreshold", 0.03, 0, 0.05],
		[ "modParameterGroupFloat", "/TXWallpaper_1/drawAlpha", [
			["float", "/TXWallpaper_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXWallpaper_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/drawAlpha/useExtMod", 0],
			["float", "/TXWallpaper_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/drawPosX", [
			["float", "/TXWallpaper_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXWallpaper_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/drawPosX/useExtMod", 0],
			["float", "/TXWallpaper_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/drawPosY", [
			["float", "/TXWallpaper_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXWallpaper_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/drawPosY/useExtMod", 0],
			["float", "/TXWallpaper_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/drawWidth", [
			["float", "/TXWallpaper_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXWallpaper_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/drawWidth/useExtMod", 0],
			["float", "/TXWallpaper_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/drawHeight", [
			["float", "/TXWallpaper_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXWallpaper_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/drawHeight/useExtMod", 0],
			["float", "/TXWallpaper_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/anchorX", [
			["float", "/TXWallpaper_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXWallpaper_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/anchorX/useExtMod", 0],
			["float", "/TXWallpaper_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/anchorY", [
			["float", "/TXWallpaper_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXWallpaper_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/anchorY/useExtMod", 0],
			["float", "/TXWallpaper_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXWallpaper_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/rotate", [
			["float", "/TXWallpaper_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXWallpaper_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/rotate/useExtMod", 0],
			["float", "/TXWallpaper_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/rotate/softMin", -360, -360, 360],
			["float", "/TXWallpaper_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/rotateMultiply", [
			["float", "/TXWallpaper_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXWallpaper_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/rotateMultiply/useExtMod", 0],
			["float", "/TXWallpaper_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXWallpaper_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/scaleX", [
			["float", "/TXWallpaper_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXWallpaper_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/scaleX/useExtMod", 0],
			["float", "/TXWallpaper_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXWallpaper_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXWallpaper_1/scaleY", [
			["float", "/TXWallpaper_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXWallpaper_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXWallpaper_1/scaleY/useExtMod", 0],
			["float", "/TXWallpaper_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXWallpaper_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXWallpaper_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXWallpaper_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
 /* ---------- TXV Outputs list for: TXColorize_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Wallpaper", {arg parameterName;
				var arrNames = ["wallpaperGroup",  "tileSize",  "tileAngle", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Render", {arg parameterName;
				parameterName.containsi("render");
			}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
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
		^"Source Image";
	}

}


