// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_MeshDistort : TXV_Module {
	classvar <defaultName = "TXV Mesh Distort";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	// add text array to ..."/TXMeshDistort_1/wave1Type/fixedValue",
	//       & "/TXMeshDistort_1/wave2Type/fixedValue"
	//       & "/TXMeshDistort_1/wave3Type/fixedValue"
	// , ["Sine", "Cosine", "Square", "Ramp Up", "Ramp Down", "Triangle", "Perlin"]

	// add text array to ..."light1Type/fixedValue", "light2Type/fixedValue", "light3Type/fixedValue"
	// , ["Point","Directional","Spot"]

	// add text array to ..."cam1PositionMode/fixedValue":
	// , ["Use cameraPositionXYZ", "Continuous truck/boom/dolly (units per sec) with Freeze & Reset", "Triggered truck/boom/dolly (units per trigger) with Freeze & Reset", "Orbit around cameraOrbitPointXYZ", ]

	// add text array to ..."cam1OrientationMode/fixedValue":
	// , ["Use cameraRotationXYZ", "Continuous Tilt/Pan/Roll (degrees per sec) with Freeze & Reset", "Triggered Tilt/Pan/Roll (degrees per trigger) with Freeze & Reset", "Use cameraLookAtPointXYZ", ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXMeshDistort_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXMeshDistort_1/drawActive", [
			["bool_int", "/TXMeshDistort_1/drawActive/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/drawActive/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXMeshDistort_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXMeshDistort_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXMeshDistort_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXMeshDistort_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXMeshDistort_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXMeshDistort_1/drawLayersRule", [
			["int", "/TXMeshDistort_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXMeshDistort_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawLayersRule/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXMeshDistort_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/renderContinuosly", [
			["bool_int", "/TXMeshDistort_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/renderContinuosly/useExtMod", 0],
			["float", "/TXMeshDistort_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/renderNow", [
			["bool_int", "/TXMeshDistort_1/renderNow/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/renderNow/useExtMod", 0],
			["float", "/TXMeshDistort_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/clearBeforeRender", [
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/clearNow", [
			["bool_int", "/TXMeshDistort_1/clearNow/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/clearNow/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorHue", [
			["float", "/TXMeshDistort_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorSaturation", [
			["float", "/TXMeshDistort_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorBrightness", [
			["float", "/TXMeshDistort_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorAlpha", [
			["float", "/TXMeshDistort_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/distortionActive", [
			["bool_int", "/TXMeshDistort_1/distortionActive/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/distortionActive/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/distortionActive/useExtMod", 0],
			["float", "/TXMeshDistort_1/distortionActive/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/distortAfterMatrix", [
			["bool_int", "/TXMeshDistort_1/distortAfterMatrix/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/distortAfterMatrix/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/distortAfterMatrix/useExtMod", 0],
			["float", "/TXMeshDistort_1/distortAfterMatrix/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/wave1Type", [
			["int", "/TXMeshDistort_1/wave1Type/fixedValue", 0, 0, 6, ["Sine", "Cosine", "Square", "Ramp Up", "Ramp Down", "Triangle", "Perlin"]],
			["float", "/TXMeshDistort_1/wave1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/wave1Type/softMin", 0, 0, 6],
			["int", "/TXMeshDistort_1/wave1Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave1Phase", [
			["float", "/TXMeshDistort_1/wave1Phase/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1Phase/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1Phase/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1Phase/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave1Phase/softMax", 1, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave1Freq", [
			["float", "/TXMeshDistort_1/wave1Freq/fixedValue", 4, 0, 20],
			["float", "/TXMeshDistort_1/wave1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1Freq/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1Freq/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1Freq/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave1Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave1AmpX", [
			["float", "/TXMeshDistort_1/wave1AmpX/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave1AmpX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1AmpX/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1AmpX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1AmpX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave1AmpX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave1AmpY", [
			["float", "/TXMeshDistort_1/wave1AmpY/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave1AmpY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1AmpY/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1AmpY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1AmpY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave1AmpY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave1AmpZ", [
			["float", "/TXMeshDistort_1/wave1AmpZ/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave1AmpZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave1AmpZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave1AmpZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave1AmpZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave1AmpZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/wave2Type", [
			["int", "/TXMeshDistort_1/wave2Type/fixedValue", 0, 0, 6, ["Sine", "Cosine", "Square", "Ramp Up", "Ramp Down", "Triangle", "Perlin"]],
			["float", "/TXMeshDistort_1/wave2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/wave2Type/softMin", 0, 0, 6],
			["int", "/TXMeshDistort_1/wave2Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave2Phase", [
			["float", "/TXMeshDistort_1/wave2Phase/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2Phase/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2Phase/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2Phase/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave2Phase/softMax", 1, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave2Freq", [
			["float", "/TXMeshDistort_1/wave2Freq/fixedValue", 4, 0, 20],
			["float", "/TXMeshDistort_1/wave2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2Freq/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2Freq/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2Freq/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave2Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave2AmpX", [
			["float", "/TXMeshDistort_1/wave2AmpX/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave2AmpX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2AmpX/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2AmpX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2AmpX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave2AmpX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave2AmpY", [
			["float", "/TXMeshDistort_1/wave2AmpY/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave2AmpY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2AmpY/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2AmpY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2AmpY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave2AmpY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave2AmpZ", [
			["float", "/TXMeshDistort_1/wave2AmpZ/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave2AmpZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave2AmpZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave2AmpZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave2AmpZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave2AmpZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/wave3Type", [
			["int", "/TXMeshDistort_1/wave3Type/fixedValue", 0, 0, 6, ["Sine", "Cosine", "Square", "Ramp Up", "Ramp Down", "Triangle", "Perlin"]],
			["float", "/TXMeshDistort_1/wave3Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/wave3Type/softMin", 0, 0, 6],
			["int", "/TXMeshDistort_1/wave3Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave3Phase", [
			["float", "/TXMeshDistort_1/wave3Phase/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3Phase/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3Phase/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3Phase/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave3Phase/softMax", 1, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave3Freq", [
			["float", "/TXMeshDistort_1/wave3Freq/fixedValue", 4, 0, 20],
			["float", "/TXMeshDistort_1/wave3Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3Freq/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3Freq/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3Freq/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave3Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave3AmpX", [
			["float", "/TXMeshDistort_1/wave3AmpX/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave3AmpX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3AmpX/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3AmpX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3AmpX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave3AmpX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave3AmpY", [
			["float", "/TXMeshDistort_1/wave3AmpY/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave3AmpY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3AmpY/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3AmpY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3AmpY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave3AmpY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave3AmpZ", [
			["float", "/TXMeshDistort_1/wave3AmpZ/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave3AmpZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave3AmpZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave3AmpZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave3AmpZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave3AmpZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/wave4Type", [
			["int", "/TXMeshDistort_1/wave4Type/fixedValue", 0, 0, 6, ["Sine", "Cosine", "Square", "Ramp Up", "Ramp Down", "Triangle", "Perlin"]],
			["float", "/TXMeshDistort_1/wave4Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/wave4Type/softMin", 0, 0, 6],
			["int", "/TXMeshDistort_1/wave4Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave4Phase", [
			["float", "/TXMeshDistort_1/wave4Phase/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4Phase/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4Phase/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4Phase/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave4Phase/softMax", 1, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave4Freq", [
			["float", "/TXMeshDistort_1/wave4Freq/fixedValue", 4, 0, 20],
			["float", "/TXMeshDistort_1/wave4Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4Freq/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4Freq/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4Freq/softMin", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave4Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave4AmpX", [
			["float", "/TXMeshDistort_1/wave4AmpX/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave4AmpX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4AmpX/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4AmpX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4AmpX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave4AmpX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave4AmpY", [
			["float", "/TXMeshDistort_1/wave4AmpY/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave4AmpY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4AmpY/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4AmpY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4AmpY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave4AmpY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/wave4AmpZ", [
			["float", "/TXMeshDistort_1/wave4AmpZ/fixedValue", 0, 0, 100],
			["float", "/TXMeshDistort_1/wave4AmpZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/wave4AmpZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/wave4AmpZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/wave4AmpZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/wave4AmpZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/useDepthTesting", [
			["bool_int", "/TXMeshDistort_1/useDepthTesting/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/useDepthTesting/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/useDepthTesting/useExtMod", 0],
			["float", "/TXMeshDistort_1/useDepthTesting/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/useLighting", [
			["bool_int", "/TXMeshDistort_1/useLighting/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/useLighting/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/useLighting/useExtMod", 0],
			["float", "/TXMeshDistort_1/useLighting/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/smoothLighting", 1],
		["bool_int", "/TXMeshDistort_1/drawLights", 0],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light1Active", [
			["bool_int", "/TXMeshDistort_1/light1Active/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/light1Active/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light1Active/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/alpha", [
			["float", "/TXMeshDistort_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/alpha/useExtMod", 0],
			["float", "/TXMeshDistort_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/alpha/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawPosX", [
			["float", "/TXMeshDistort_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawPosX/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawPosY", [
			["float", "/TXMeshDistort_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawPosY/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawWidth", [
			["float", "/TXMeshDistort_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawWidth/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawHeight", [
			["float", "/TXMeshDistort_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawHeight/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/anchorX", [
			["float", "/TXMeshDistort_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/anchorX/useExtMod", 0],
			["float", "/TXMeshDistort_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/anchorY", [
			["float", "/TXMeshDistort_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/anchorY/useExtMod", 0],
			["float", "/TXMeshDistort_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/rotate", [
			["float", "/TXMeshDistort_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/rotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/rotate/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/rotateMultiply", [
			["float", "/TXMeshDistort_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXMeshDistort_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/rotateMultiply/useExtMod", 0],
			["float", "/TXMeshDistort_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXMeshDistort_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/scaleX", [
			["float", "/TXMeshDistort_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXMeshDistort_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/scaleX/useExtMod", 0],
			["float", "/TXMeshDistort_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXMeshDistort_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/scaleY", [
			["float", "/TXMeshDistort_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXMeshDistort_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/scaleY/useExtMod", 0],
			["float", "/TXMeshDistort_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXMeshDistort_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXMeshDistort_1/useScaleXForScaleY", 0],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light1Type", [
			["int", "/TXMeshDistort_1/light1Type/fixedValue", 0, 0, 2, ["Point","Directional","Spot"]],
			["float", "/TXMeshDistort_1/light1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light1Type/softMin", 0, 0, 2],
			["int", "/TXMeshDistort_1/light1Type/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1DiffuseColorHue", [
			["float", "/TXMeshDistort_1/light1DiffuseColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1DiffuseColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1DiffuseColorHueRotate", [
			["float", "/TXMeshDistort_1/light1DiffuseColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1DiffuseColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1DiffuseColorSaturation", [
			["float", "/TXMeshDistort_1/light1DiffuseColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1DiffuseColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1DiffuseColorBrightness", [
			["float", "/TXMeshDistort_1/light1DiffuseColorBrightness/fixedValue", 0.4, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1DiffuseColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1DiffuseColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light1DiffuseColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light1DiffuseColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1DiffuseColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light1UseDiffuseColorForAmbient", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1AmbientColorHue", [
			["float", "/TXMeshDistort_1/light1AmbientColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1AmbientColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1AmbientColorHueRotate", [
			["float", "/TXMeshDistort_1/light1AmbientColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1AmbientColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1AmbientColorSaturation", [
			["float", "/TXMeshDistort_1/light1AmbientColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1AmbientColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1AmbientColorBrightness", [
			["float", "/TXMeshDistort_1/light1AmbientColorBrightness/fixedValue", 0.185, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1AmbientColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1AmbientColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light1AmbientColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light1AmbientColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light1AmbientColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1AmbientColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light1UseDiffuseColorForSpecular", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1SpecularColorHue", [
			["float", "/TXMeshDistort_1/light1SpecularColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpecularColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1SpecularColorHueRotate", [
			["float", "/TXMeshDistort_1/light1SpecularColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpecularColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1SpecularColorSaturation", [
			["float", "/TXMeshDistort_1/light1SpecularColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpecularColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1SpecularColorBrightness", [
			["float", "/TXMeshDistort_1/light1SpecularColorBrightness/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpecularColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpecularColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light1SpecularColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light1SpecularColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light1SpecularColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpecularColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1PositionX", [
			["float", "/TXMeshDistort_1/light1PositionX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light1PositionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1PositionX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1PositionX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1PositionX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light1PositionX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1PositionY", [
			["float", "/TXMeshDistort_1/light1PositionY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light1PositionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1PositionY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1PositionY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1PositionY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light1PositionY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1PositionZ", [
			["float", "/TXMeshDistort_1/light1PositionZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light1PositionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1PositionZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1PositionZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1PositionZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light1PositionZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1OrientationX", [
			["float", "/TXMeshDistort_1/light1OrientationX/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1OrientationX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1OrientationX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1OrientationX/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1OrientationY", [
			["float", "/TXMeshDistort_1/light1OrientationY/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1OrientationY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1OrientationY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1OrientationY/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1OrientationZ", [
			["float", "/TXMeshDistort_1/light1OrientationZ/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1OrientationZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1OrientationZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1OrientationZ/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light1OrientationZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light1SpotConcentration", [
			["int", "/TXMeshDistort_1/light1SpotConcentration/fixedValue", 64, 0, 128],
			["float", "/TXMeshDistort_1/light1SpotConcentration/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpotConcentration/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpotConcentration/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light1SpotConcentration/softMin", 0, 0, 128],
			["int", "/TXMeshDistort_1/light1SpotConcentration/softMax", 128, 0, 128],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light1SpotlightAngle", [
			["float", "/TXMeshDistort_1/light1SpotlightAngle/fixedValue", 0, 0, 90],
			["float", "/TXMeshDistort_1/light1SpotlightAngle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light1SpotlightAngle/useExtMod", 0],
			["float", "/TXMeshDistort_1/light1SpotlightAngle/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light1SpotlightAngle/softMin", 0, 0, 90],
			["float", "/TXMeshDistort_1/light1SpotlightAngle/softMax", 90, 0, 90],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light2Active", [
			["bool_int", "/TXMeshDistort_1/light2Active/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light2Active/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light2Active/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light2Type", [
			["int", "/TXMeshDistort_1/light2Type/fixedValue", 0, 0, 2, ["Point","Directional","Spot"]],
			["float", "/TXMeshDistort_1/light2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light2Type/softMin", 0, 0, 2],
			["int", "/TXMeshDistort_1/light2Type/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2DiffuseColorHue", [
			["float", "/TXMeshDistort_1/light2DiffuseColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2DiffuseColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2DiffuseColorHueRotate", [
			["float", "/TXMeshDistort_1/light2DiffuseColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2DiffuseColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2DiffuseColorSaturation", [
			["float", "/TXMeshDistort_1/light2DiffuseColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2DiffuseColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2DiffuseColorBrightness", [
			["float", "/TXMeshDistort_1/light2DiffuseColorBrightness/fixedValue", 0.4, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2DiffuseColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2DiffuseColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light2DiffuseColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light2DiffuseColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2DiffuseColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light2UseDiffuseColorForAmbient", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2AmbientColorHue", [
			["float", "/TXMeshDistort_1/light2AmbientColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2AmbientColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2AmbientColorHueRotate", [
			["float", "/TXMeshDistort_1/light2AmbientColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2AmbientColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2AmbientColorSaturation", [
			["float", "/TXMeshDistort_1/light2AmbientColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2AmbientColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2AmbientColorBrightness", [
			["float", "/TXMeshDistort_1/light2AmbientColorBrightness/fixedValue", 0.185, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2AmbientColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2AmbientColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light2AmbientColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light2AmbientColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light2AmbientColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2AmbientColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light2UseDiffuseColorForSpecular", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2SpecularColorHue", [
			["float", "/TXMeshDistort_1/light2SpecularColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpecularColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2SpecularColorHueRotate", [
			["float", "/TXMeshDistort_1/light2SpecularColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpecularColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2SpecularColorSaturation", [
			["float", "/TXMeshDistort_1/light2SpecularColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpecularColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2SpecularColorBrightness", [
			["float", "/TXMeshDistort_1/light2SpecularColorBrightness/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpecularColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpecularColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light2SpecularColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light2SpecularColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light2SpecularColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpecularColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2PositionX", [
			["float", "/TXMeshDistort_1/light2PositionX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light2PositionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2PositionX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2PositionX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2PositionX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light2PositionX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2PositionY", [
			["float", "/TXMeshDistort_1/light2PositionY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light2PositionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2PositionY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2PositionY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2PositionY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light2PositionY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2PositionZ", [
			["float", "/TXMeshDistort_1/light2PositionZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light2PositionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2PositionZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2PositionZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2PositionZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light2PositionZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2OrientationX", [
			["float", "/TXMeshDistort_1/light2OrientationX/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2OrientationX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2OrientationX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2OrientationX/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2OrientationY", [
			["float", "/TXMeshDistort_1/light2OrientationY/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2OrientationY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2OrientationY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2OrientationY/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2OrientationZ", [
			["float", "/TXMeshDistort_1/light2OrientationZ/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2OrientationZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2OrientationZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2OrientationZ/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light2OrientationZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light2SpotConcentration", [
			["int", "/TXMeshDistort_1/light2SpotConcentration/fixedValue", 64, 0, 128],
			["float", "/TXMeshDistort_1/light2SpotConcentration/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpotConcentration/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpotConcentration/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light2SpotConcentration/softMin", 0, 0, 128],
			["int", "/TXMeshDistort_1/light2SpotConcentration/softMax", 128, 0, 128],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light2SpotlightAngle", [
			["float", "/TXMeshDistort_1/light2SpotlightAngle/fixedValue", 0, 0, 90],
			["float", "/TXMeshDistort_1/light2SpotlightAngle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light2SpotlightAngle/useExtMod", 0],
			["float", "/TXMeshDistort_1/light2SpotlightAngle/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light2SpotlightAngle/softMin", 0, 0, 90],
			["float", "/TXMeshDistort_1/light2SpotlightAngle/softMax", 90, 0, 90],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light3Active", [
			["bool_int", "/TXMeshDistort_1/light3Active/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light3Active/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light3Active/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light3Type", [
			["int", "/TXMeshDistort_1/light3Type/fixedValue", 0, 0, 2, ["Point","Directional","Spot"]],
			["float", "/TXMeshDistort_1/light3Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3Type/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3Type/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light3Type/softMin", 0, 0, 2],
			["int", "/TXMeshDistort_1/light3Type/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3DiffuseColorHue", [
			["float", "/TXMeshDistort_1/light3DiffuseColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3DiffuseColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3DiffuseColorHueRotate", [
			["float", "/TXMeshDistort_1/light3DiffuseColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3DiffuseColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3DiffuseColorSaturation", [
			["float", "/TXMeshDistort_1/light3DiffuseColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3DiffuseColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3DiffuseColorBrightness", [
			["float", "/TXMeshDistort_1/light3DiffuseColorBrightness/fixedValue", 0.4, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3DiffuseColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3DiffuseColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light3DiffuseColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light3DiffuseColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3DiffuseColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light3UseDiffuseColorForAmbient", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3AmbientColorHue", [
			["float", "/TXMeshDistort_1/light3AmbientColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3AmbientColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3AmbientColorHueRotate", [
			["float", "/TXMeshDistort_1/light3AmbientColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3AmbientColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3AmbientColorSaturation", [
			["float", "/TXMeshDistort_1/light3AmbientColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3AmbientColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3AmbientColorBrightness", [
			["float", "/TXMeshDistort_1/light3AmbientColorBrightness/fixedValue", 0.185, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3AmbientColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3AmbientColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light3AmbientColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light3AmbientColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light3AmbientColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3AmbientColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXMeshDistort_1/light3UseDiffuseColorForSpecular", 0],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3SpecularColorHue", [
			["float", "/TXMeshDistort_1/light3SpecularColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpecularColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3SpecularColorHueRotate", [
			["float", "/TXMeshDistort_1/light3SpecularColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorHueRotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpecularColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3SpecularColorSaturation", [
			["float", "/TXMeshDistort_1/light3SpecularColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpecularColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3SpecularColorBrightness", [
			["float", "/TXMeshDistort_1/light3SpecularColorBrightness/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpecularColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpecularColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/light3SpecularColorHSBAsRGB", [
			["bool_int", "/TXMeshDistort_1/light3SpecularColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/light3SpecularColorHSBAsRGB/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpecularColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3PositionX", [
			["float", "/TXMeshDistort_1/light3PositionX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light3PositionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3PositionX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3PositionX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3PositionX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light3PositionX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3PositionY", [
			["float", "/TXMeshDistort_1/light3PositionY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light3PositionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3PositionY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3PositionY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3PositionY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light3PositionY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3PositionZ", [
			["float", "/TXMeshDistort_1/light3PositionZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/light3PositionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3PositionZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3PositionZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3PositionZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/light3PositionZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3OrientationX", [
			["float", "/TXMeshDistort_1/light3OrientationX/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3OrientationX/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3OrientationX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3OrientationX/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3OrientationY", [
			["float", "/TXMeshDistort_1/light3OrientationY/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3OrientationY/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3OrientationY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3OrientationY/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3OrientationZ", [
			["float", "/TXMeshDistort_1/light3OrientationZ/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3OrientationZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3OrientationZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3OrientationZ/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/light3OrientationZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/light3SpotConcentration", [
			["int", "/TXMeshDistort_1/light3SpotConcentration/fixedValue", 64, 0, 128],
			["float", "/TXMeshDistort_1/light3SpotConcentration/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpotConcentration/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpotConcentration/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/light3SpotConcentration/softMin", 0, 0, 128],
			["int", "/TXMeshDistort_1/light3SpotConcentration/softMax", 128, 0, 128],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/light3SpotlightAngle", [
			["float", "/TXMeshDistort_1/light3SpotlightAngle/fixedValue", 0, 0, 90],
			["float", "/TXMeshDistort_1/light3SpotlightAngle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/light3SpotlightAngle/useExtMod", 0],
			["float", "/TXMeshDistort_1/light3SpotlightAngle/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/light3SpotlightAngle/softMin", 0, 0, 90],
			["float", "/TXMeshDistort_1/light3SpotlightAngle/softMax", 90, 0, 90],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1Active", [
			["bool_int", "/TXMeshDistort_1/cam1Active/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1Active/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1Active/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/cam1PositionMode", [
			["int", "/TXMeshDistort_1/cam1PositionMode/fixedValue", 0, 0, 3, ["Use cameraPositionXYZ", "Continuous truck/boom/dolly (units per sec) with Freeze & Reset", "Triggered truck/boom/dolly (units per trigger) with Freeze & Reset", "Orbit around cameraOrbitPointXYZ", ]],
			["float", "/TXMeshDistort_1/cam1PositionMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1PositionMode/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PositionMode/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/cam1PositionMode/softMin", 0, 0, 3],
			["int", "/TXMeshDistort_1/cam1PositionMode/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1PositionX", [
			["float", "/TXMeshDistort_1/cam1PositionX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1PositionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1PositionX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PositionX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1PositionX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1PositionX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1PositionY", [
			["float", "/TXMeshDistort_1/cam1PositionY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1PositionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1PositionY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PositionY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1PositionY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1PositionY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1PositionZ", [
			["float", "/TXMeshDistort_1/cam1PositionZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1PositionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1PositionZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PositionZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1PositionZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1PositionZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1TruckX", [
			["float", "/TXMeshDistort_1/cam1TruckX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1TruckX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1TruckX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1TruckX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1TruckX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1TruckX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1TruckXTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1TruckXTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1TruckXTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1TruckXTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1TruckXTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1BoomY", [
			["float", "/TXMeshDistort_1/cam1BoomY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1BoomY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1BoomY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1BoomY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1BoomY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1BoomY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1BoomYTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1BoomYTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1BoomYTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1BoomYTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1BoomYTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1DollyZ", [
			["float", "/TXMeshDistort_1/cam1DollyZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1DollyZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1DollyZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1DollyZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1DollyZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1DollyZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1DollyZTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1DollyZTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1DollyZTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1DollyZTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1DollyZTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1FreezePosition", [
			["bool_int", "/TXMeshDistort_1/cam1FreezePosition/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1FreezePosition/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1FreezePosition/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1FreezePosition/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1ResetToPositionXYZ", [
			["bool_int", "/TXMeshDistort_1/cam1ResetToPositionXYZ/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1ResetToPositionXYZ/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1ResetToPositionXYZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1ResetToPositionXYZ/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitPointX", [
			["float", "/TXMeshDistort_1/cam1OrbitPointX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1OrbitPointX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitPointX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitPointX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitPointX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1OrbitPointX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitPointY", [
			["float", "/TXMeshDistort_1/cam1OrbitPointY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1OrbitPointY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitPointY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitPointY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitPointY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1OrbitPointY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitPointZ", [
			["float", "/TXMeshDistort_1/cam1OrbitPointZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1OrbitPointZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitPointZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitPointZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitPointZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1OrbitPointZ/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitLongitude", [
			["float", "/TXMeshDistort_1/cam1OrbitLongitude/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1OrbitLongitude/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitLongitude/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitLongitude/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitLongitude/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1OrbitLongitude/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitLatitude", [
			["float", "/TXMeshDistort_1/cam1OrbitLatitude/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1OrbitLatitude/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitLatitude/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitLatitude/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitLatitude/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1OrbitLatitude/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1OrbitRadius", [
			["float", "/TXMeshDistort_1/cam1OrbitRadius/fixedValue", 0, 0, 1000],
			["float", "/TXMeshDistort_1/cam1OrbitRadius/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrbitRadius/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrbitRadius/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1OrbitRadius/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/cam1OrbitRadius/softMax", 1000, 0, 10000],
		]],
		[ "modParameterGroupInt", "/TXMeshDistort_1/cam1OrientationMode", [
			["int", "/TXMeshDistort_1/cam1OrientationMode/fixedValue", 0, 0, 3, ["Use cameraRotationXYZ", "Continuous Tilt/Pan/Roll (degrees per sec) with Freeze & Reset", "Triggered Tilt/Pan/Roll (degrees per trigger) with Freeze & Reset", "Use cameraLookAtPointXYZ", ]],
			["float", "/TXMeshDistort_1/cam1OrientationMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1OrientationMode/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1OrientationMode/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/cam1OrientationMode/softMin", 0, 0, 3],
			["int", "/TXMeshDistort_1/cam1OrientationMode/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1RotationX", [
			["float", "/TXMeshDistort_1/cam1RotationX/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1RotationX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1RotationX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1RotationX/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1RotationY", [
			["float", "/TXMeshDistort_1/cam1RotationY/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1RotationY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1RotationY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1RotationY/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1RotationZ", [
			["float", "/TXMeshDistort_1/cam1RotationZ/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1RotationZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1RotationZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1RotationZ/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1RotationZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1TiltX", [
			["float", "/TXMeshDistort_1/cam1TiltX/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1TiltX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1TiltX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1TiltX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1TiltX/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1TiltX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1TiltXTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1TiltXTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1TiltXTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1TiltXTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1TiltXTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1PanY", [
			["float", "/TXMeshDistort_1/cam1PanY/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1PanY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1PanY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PanY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1PanY/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1PanY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1PanYTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1PanYTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1PanYTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1PanYTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1PanYTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1RollZ", [
			["float", "/TXMeshDistort_1/cam1RollZ/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/cam1RollZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1RollZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1RollZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1RollZ/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/cam1RollZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1RollZTrigger", [
			["bool_int", "/TXMeshDistort_1/cam1RollZTrigger/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1RollZTrigger/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1RollZTrigger/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1RollZTrigger/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1FreezeRotation", [
			["bool_int", "/TXMeshDistort_1/cam1FreezeRotation/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1FreezeRotation/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1FreezeRotation/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1FreezeRotation/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/cam1ResetToRotationXYZ", [
			["bool_int", "/TXMeshDistort_1/cam1ResetToRotationXYZ/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/cam1ResetToRotationXYZ/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/cam1ResetToRotationXYZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1ResetToRotationXYZ/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1LookAtPointX", [
			["float", "/TXMeshDistort_1/cam1LookAtPointX/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1LookAtPointX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1LookAtPointX/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1LookAtPointX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1LookAtPointX/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1LookAtPointX/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1LookAtPointY", [
			["float", "/TXMeshDistort_1/cam1LookAtPointY/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1LookAtPointY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1LookAtPointY/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1LookAtPointY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1LookAtPointY/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1LookAtPointY/softMax", 1000, -10000, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/cam1LookAtPointZ", [
			["float", "/TXMeshDistort_1/cam1LookAtPointZ/fixedValue", 0, -1000, 1000],
			["float", "/TXMeshDistort_1/cam1LookAtPointZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/cam1LookAtPointZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/cam1LookAtPointZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/cam1LookAtPointZ/softMin", -1000, -10000, 10000],
			["float", "/TXMeshDistort_1/cam1LookAtPointZ/softMax", 1000, -10000, 10000],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXMeshDistort_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		// var cam1ViewNames = ["cam1Orthographic", "cam1FieldOfViewDegrees", "cam1NearClipDistance", "cam1FarClipDistance", "cam1LensOffsetX", "cam1LensOffsetY", "cam1ForceAspectRatio", "cam1AspectRatioWidth", "cam1AspectRatioHeight", ];
		var cam1PosNames = ["cam1PositionMode", "cam1PositionX", "cam1PositionY", "cam1PositionZ", ];
		var cam1TruckBoomDollyNames = ["cam1PositionMode", "cam1TruckX", "cam1TruckXTrigger", "cam1BoomY", "cam1BoomYTrigger", "cam1DollyZ", "cam1DollyZTrigger", "cam1FreezePosition", "cam1ResetToPositionXYZ", ];
		var cam1OrbitNames = ["cam1PositionMode", "cam1OrbitPointX", "cam1OrbitPointY", "cam1OrbitPointZ", "cam1OrbitLongitude", "cam1OrbitLatitude", "cam1OrbitRadius", ];
		var cam1OrientRotationNames = ["cam1OrientationMode", "cam1RotationX", "cam1RotationY", "cam1RotationZ", ];
		var cam1TiltPanRollNames = ["cam1TiltX", "cam1TiltXTrigger", "cam1PanY", "cam1PanYTrigger", "cam1RollZ", "cam1RollZTrigger", "cam1FreezeRotation", "cam1ResetToRotationXYZ", ];
		var cam2ViewNames = ["cam2Orthographic", "cam2FieldOfViewDegrees", "cam2NearClipDistance", "cam2FarClipDistance", "cam2LensOffsetX", "cam2LensOffsetY", "cam2ForceAspectRatio", "cam2AspectRatioWidth", "cam2AspectRatioHeight", ];
		var cam2PosNames = ["cam2PositionMode", "cam2PositionX", "cam2PositionY", "cam2PositionZ", ];
		var cam2TruckBoomDollyNames = ["cam2PositionMode", "cam2TruckX", "cam2TruckXTrigger", "cam2BoomY", "cam2BoomYTrigger", "cam2DollyZ", "cam2DollyZTrigger", "cam2FreezePosition", "cam2ResetToPositionXYZ", ];
		var cam2OrbitNames = ["cam2PositionMode", "cam2OrbitPointX", "cam2OrbitPointY", "cam2OrbitPointZ", "cam2OrbitLongitude", "cam2OrbitLatitude", "cam2OrbitRadius", ];
		var cam2OrientRotationNames = ["cam2OrientationMode", "cam2RotationX", "cam2RotationY", "cam2RotationZ", ];
		var cam2TiltPanRollNames = ["cam2TiltX", "cam2TiltXTrigger", "cam2PanY", "cam2PanYTrigger", "cam2RollZ", "cam2RollZTrigger", "cam2FreezeRotation", "cam2ResetToRotationXYZ",];
		^[
			// format: ["Example Name", nameValidityFunction],
			["Distortion$Active & Wave 1", {arg parameterName;
				parameterName.containsi("Distort")
				|| parameterName.containsi("wave1")
				;}],
			["Distortion$Wave 2", {arg parameterName; parameterName.containsi("wave2");}],
			["Distortion$Wave 3", {arg parameterName; parameterName.containsi("wave3");}],
			["Distortion$Wave 4", {arg parameterName; parameterName.containsi("wave4");}],

			["Light 1$Type", {arg argName;
				argName.contains("light1")
				&& not(argName.containsi("Color"))
				&& not(argName.containsi("Position"))
				&& not(argName.containsi("Orientation"))
			;}],
			["Light 1$Position, Orientation", {arg argName;
				argName.contains("light1Position")
				|| argName.containsi("light1Orientation")
			;}],
			["Light 1$Diffuse Color", {arg argName; argName.contains("light1DiffuseColor");}],
			["Light 1$Ambient Color", {arg argName; argName.contains("light1")
				&& argName.containsi("Ambient");}],
			["Light 1$Specular Color", {arg argName; argName.contains("light1")
				&& argName.containsi("Specular");}],

			["Light 2$Type", {arg argName;
				argName.contains("light2")
				&& not(argName.containsi("Color"))
				&& not(argName.containsi("Position"))
				&& not(argName.containsi("Orientation"))
			;}],
			["Light 2$Position, Orientation", {arg argName;
				argName.contains("light2Position")
				|| argName.containsi("light2Orientation")
			;}],
			["Light 2$Diffuse Color", {arg argName; argName.contains("light2DiffuseColor");}],
			["Light 2$Ambient Color", {arg argName; argName.contains("light2")
				&& argName.containsi("Ambient");}],
			["Light 2$Specular Color", {arg argName; argName.contains("light2")
				&& argName.containsi("Specular");}],

			["Light 3$Type", {arg argName;
				argName.contains("light3")
				&& not(argName.containsi("Color"))
				&& not(argName.containsi("Position"))
				&& not(argName.containsi("Orientation"))
			;}],
			["Light 3$Position, Orientation", {arg argName;
				argName.contains("light3Position")
				|| argName.containsi("light3Orientation")
			;}],
			["Light 3$Diffuse Color", {arg argName; argName.contains("light3DiffuseColor");}],
			["Light 3$Ambient Color", {arg argName; argName.contains("light3")
				&& argName.containsi("Ambient");}],
			["Light 3$Specular Color", {arg argName; argName.contains("light3")
				&& argName.containsi("Specular");}],

			// ["Camera$View", {arg argName; cam1ViewNames.indexOfEqual(argName).notNil;}],
			["Camera$Position & Mode", {arg argName; cam1PosNames.indexOfEqual(argName).notNil;}],
			["Camera$Pos: Truck, Boom, Dolly", {arg argName; cam1TruckBoomDollyNames.indexOfEqual(argName).notNil;}],
			["Camera$Pos: Orbit", {arg argName; cam1OrbitNames.indexOfEqual(argName).notNil;}],
			["Camera$Orient: Mode & Rotation", {arg argName; cam1OrientRotationNames.indexOfEqual(argName).notNil;}],
			["Camera$Orient: Tilt, Pan, Roll", {arg argName; cam1TiltPanRollNames.indexOfEqual(argName).notNil;}],
			["Camera$Orient: Look At Point", {arg argName; argName.containsi("cam1LookAtPoint");}],
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
					var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule", "maxDepthRule",
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
		^"Global";
	}

}


