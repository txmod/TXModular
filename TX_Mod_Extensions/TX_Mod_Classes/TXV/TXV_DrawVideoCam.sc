// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawVideoCam : TXV_Module {
	classvar <defaultName = "TXV VideoCam";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawVideoCam_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawVideoCam_1/drawActive", [
			["bool_int", "/TXDrawVideoCam_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawVideoCam_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawVideoCam_1/drawActive/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawVideoCam_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawVideoCam_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawVideoCam_1/customMaxHeight", 768, 1, 4096],
		// ["int", "/TXDrawVideoCam_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXDrawVideoCam_1/videoCamActive", 1],
		["int", "/TXDrawVideoCam_1/videoDeviceIndex", 0, 0, 10],
		["int", "/TXDrawVideoCam_1/cameraViewWidth", 640, 10, 5000],
		["int", "/TXDrawVideoCam_1/cameraViewHeight", 480, 10, 5000],
		["bool_int", "/TXDrawVideoCam_1/useSamplePosForDrawPos", 0],
		["bool_int", "/TXDrawVideoCam_1/useSampleSizeForDrawSize", 0],
		["bool_int", "/TXDrawVideoCam_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDrawVideoCam_1/constrainToEdges", 0],
		["float", "/TXDrawVideoCam_1/alphaThreshold", 0.03, 0, 0.05],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/alpha", [
			["float", "/TXDrawVideoCam_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawVideoCam_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/alpha/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/alpha/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/samplePosX", [
			["float", "/TXDrawVideoCam_1/samplePosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/samplePosX/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/samplePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosX/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/samplePosY", [
			["float", "/TXDrawVideoCam_1/samplePosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/samplePosY/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/samplePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosY/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/samplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/sampleWidth", [
			["float", "/TXDrawVideoCam_1/sampleWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/sampleWidth/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/sampleWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleWidth/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/sampleHeight", [
			["float", "/TXDrawVideoCam_1/sampleHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/sampleHeight/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/sampleHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleHeight/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/sampleHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/drawPosX", [
			["float", "/TXDrawVideoCam_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/drawPosX/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/drawPosY", [
			["float", "/TXDrawVideoCam_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/drawPosY/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/drawWidth", [
			["float", "/TXDrawVideoCam_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawVideoCam_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/drawWidth/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/drawHeight", [
			["float", "/TXDrawVideoCam_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawVideoCam_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/drawHeight/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/anchorX", [
			["float", "/TXDrawVideoCam_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/anchorX/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/anchorY", [
			["float", "/TXDrawVideoCam_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/anchorY/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/rotate", [
			["float", "/TXDrawVideoCam_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawVideoCam_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/rotate/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawVideoCam_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/rotateMultiply", [
			["float", "/TXDrawVideoCam_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawVideoCam_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawVideoCam_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/scaleX", [
			["float", "/TXDrawVideoCam_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawVideoCam_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/scaleX/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawVideoCam_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawVideoCam_1/scaleY", [
			["float", "/TXDrawVideoCam_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawVideoCam_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawVideoCam_1/scaleY/useExtMod", 0],
			["float", "/TXDrawVideoCam_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawVideoCam_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawVideoCam_1/scaleY/softMax", 3, -10, 10],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawVideoCam_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Draw$Active & Alpha", {arg parameterName;
				var arrNames = ["drawActive", "alphaThreshold", "alpha", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Size", {arg parameterName;
				var arrNames = ["maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
					"useSampleSizeForDrawSize", "constrainToEdges","drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor & Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "useSamplePosForDrawPos", "anchorX", "anchorY",
					"rotate", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Camera & Sample Area";
	}

}


