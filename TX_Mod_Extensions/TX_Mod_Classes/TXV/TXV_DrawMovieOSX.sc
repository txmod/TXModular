// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawMovieOSX : TXV_Module {
	classvar <defaultName = "TXV MovieOSX";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."loopType/fixedValue":
	// , ["No Looping", "Forwards & Backwards Looping", "Normal Looping", ]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawMovieOSX_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/movie", "assetSlot/movie/movieAsset", 0],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/drawActive", [
			["bool_int", "/TXDrawMovieOSX_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawMovieOSX_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/drawActive/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawMovieOSX_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawMovieOSX_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawMovieOSX_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawMovieOSX_1/movieScaleMode", [
			["int", "/TXDrawMovieOSX_1/movieScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXDrawMovieOSX_1/movieScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/movieScaleMode/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/movieScaleMode/extModValue", 0, 0, 1],
			["int", "/TXDrawMovieOSX_1/movieScaleMode/softMin", 0, 0, 2],
			["int", "/TXDrawMovieOSX_1/movieScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/speed", [
			["float", "/TXDrawMovieOSX_1/speed/fixedValue", 1, -3, 3],
			["float", "/TXDrawMovieOSX_1/speed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/speed/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/speed/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/speed/softMin", -3, -10, 10],
			["float", "/TXDrawMovieOSX_1/speed/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawMovieOSX_1/readMovieAlpha", 0],
		[ "modParameterGroupInt", "/TXDrawMovieOSX_1/loopType", [
			["int", "/TXDrawMovieOSX_1/loopType/fixedValue", 3, 1, 3, ["No Looping", "Forwards & Backwards Looping", "Normal Looping", ]],
			["float", "/TXDrawMovieOSX_1/loopType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/loopType/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/loopType/extModValue", 0, 0, 1],
			["int", "/TXDrawMovieOSX_1/loopType/softMin", 1, 1, 3],
			["int", "/TXDrawMovieOSX_1/loopType/softMax", 3, 1, 3],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/synchronous", [
			["bool_int", "/TXDrawMovieOSX_1/synchronous/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/synchronous/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/synchronous/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/synchronous/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/play", [
			["bool_int", "/TXDrawMovieOSX_1/play/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/play/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/play/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/play/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/pause", [
			["bool_int", "/TXDrawMovieOSX_1/pause/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/pause/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/pause/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/pause/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/goToFirstFrame", [
			["bool_int", "/TXDrawMovieOSX_1/goToFirstFrame/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToFirstFrame/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToFirstFrame/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/goToFirstFrame/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/goToNextFrame", [
			["bool_int", "/TXDrawMovieOSX_1/goToNextFrame/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToNextFrame/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToNextFrame/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/goToNextFrame/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawMovieOSX_1/goToPreviousFrame", [
			["bool_int", "/TXDrawMovieOSX_1/goToPreviousFrame/fixedValue", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToPreviousFrame/fixedModMix", 0],
			["bool_int", "/TXDrawMovieOSX_1/goToPreviousFrame/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/goToPreviousFrame/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/position", [
			["float", "/TXDrawMovieOSX_1/position/fixedValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/position/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/position/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/position/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/position/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/position/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawMovieOSX_1/useSamplePosForDrawPos", 0],
		["bool_int", "/TXDrawMovieOSX_1/useSampleSizeForDrawSize", 0],
		["bool_int", "/TXDrawMovieOSX_1/constrainToEdges", 0],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/alpha", [
			["float", "/TXDrawMovieOSX_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawMovieOSX_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/alpha/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/alpha/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/samplePosX", [
			["float", "/TXDrawMovieOSX_1/samplePosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/samplePosX/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/samplePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosX/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/samplePosY", [
			["float", "/TXDrawMovieOSX_1/samplePosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/samplePosY/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/samplePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosY/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/samplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/sampleWidth", [
			["float", "/TXDrawMovieOSX_1/sampleWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/sampleWidth/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/sampleWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleWidth/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/sampleHeight", [
			["float", "/TXDrawMovieOSX_1/sampleHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/sampleHeight/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/sampleHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleHeight/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/sampleHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/drawPosX", [
			["float", "/TXDrawMovieOSX_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/drawPosX/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/drawPosY", [
			["float", "/TXDrawMovieOSX_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/drawPosY/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/drawWidth", [
			["float", "/TXDrawMovieOSX_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/drawWidth/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/drawHeight", [
			["float", "/TXDrawMovieOSX_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/drawHeight/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/anchorX", [
			["float", "/TXDrawMovieOSX_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/anchorX/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/anchorY", [
			["float", "/TXDrawMovieOSX_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/anchorY/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/rotate", [
			["float", "/TXDrawMovieOSX_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawMovieOSX_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/rotate/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawMovieOSX_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/rotateMultiply", [
			["float", "/TXDrawMovieOSX_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawMovieOSX_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawMovieOSX_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/scaleX", [
			["float", "/TXDrawMovieOSX_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawMovieOSX_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/scaleX/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawMovieOSX_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawMovieOSX_1/scaleY", [
			["float", "/TXDrawMovieOSX_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawMovieOSX_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawMovieOSX_1/scaleY/useExtMod", 0],
			["float", "/TXDrawMovieOSX_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawMovieOSX_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawMovieOSX_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawMovieOSX_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawMovieOSX_1 ---------- */
		// empty
	];}

}


