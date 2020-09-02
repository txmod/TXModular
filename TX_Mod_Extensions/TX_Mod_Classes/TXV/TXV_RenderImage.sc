// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_RenderImage : TXV_Module {
	classvar <defaultName = "TXV Render Image";
	classvar <>arrInstances;

	// add text array to ..."windowSizeMode":
	// , ["CUSTOM", "480 x 360", "858 x 480", "1280 x 720", "1920 x 1080", "2560 x 1440", "3840 x 2160", ]


	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	// add text array to ..."recordVideoCodec":
	// , ["mpeg4 - uses VideoBitRate", "ProRes LT", "ProRes Standard", "ProRes HQ", "ProRes 4444", "ProRes 4444XQ", "raw video", "h264 - uses VideoBitRate & SpeedPreset", "h264 CRF - uses ConstantRateFactor & speedPreset", "h265 - uses VideoBitRate & SpeedPreset", "h265 CRF - uses ConstantRateFactor & speedPreset", ]

	// add text array to ..."recordVideoFileExtension":
	// , ["mov", "mp4", "avi",]

	// add text array to ..."recordVideoBitrate":
	// , ["400k","800k", "1 Mbps - Youtube: 360p (SD) 480 x 360  24/25/30 fps", "1.2 Mbps", "1.5 Mbps - Youtube: 360p (SD) 480 x 360  48/50/60 fps", "2 Mbps", "2.5 Mbps - Youtube: 480p (SD) 858 x 480, 24/25/30 fps", "3 Mbps", "4 Mbps - Youtube: 480p (SD) 858 x 480, 48/50/60 fps", "5 Mbps - Youtube: 720p (Half HD)1280 x 720, 24/25/30 fps", "6 Mbps", "7 Mbps - Youtube: 720p (Half HD)1280 x 720, 48/50/60 fps", "8 Mbps - Youtube: 1080p (Full HD) 1920 x 1080, 24/25/30 fps", "10 Mbps", "12 Mbps - Youtube: 1080p (Full HD) 1920 x 1080, 48/50/60 fps", "14 Mbps","16 Mbps - Youtube: 1440p (2k) 2560 x 1440, 24/25/30 fps", "18 Mbps", "20 Mbps", "24 Mbps - Youtube: 1440p (2k) 2560 x 1440 48/50/60 fps", "30 Mbps", "35 Mbps", "40 Mbps - Youtube: 2160p (4k) 3840 x 2160, 24/25/30 fps", "45 Mbps", "50 Mbps", "55 Mbps", "60 Mbps - Youtube: 2160p (4k) 3840 x 2160, 48/50/60 fps", "70 Mbps", "80 Mbps", "90 Mbps", "100 Mbps", ]

	// add text array to ..."recordVideoH6245SpeedPreset":
	// , ["very slow", "slower", "slow", "medium (default)", "fast", "faster", "veryfast",]

	// add text array to ..."recordAudioBitrate":
	// , ["128k", "160k", "192k", "256k", "320k"]

	// add text array to ..."saveImageFormat" & "imageSeqFormat":
	// , ["jpg (May Not Work!!)", "png (slow but high res)", "bmp (fast but big)",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXRenderImage_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXRenderImage_1/drawActive", [
			["bool_int", "/TXRenderImage_1/drawActive/fixedValue", 1],
			["bool_int", "/TXRenderImage_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/drawActive/useExtMod", 0],
			["float", "/TXRenderImage_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXRenderImage_1/maxWidthHeightRule", 0, 0, 11, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]],
		["int", "/TXRenderImage_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXRenderImage_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXRenderImage_1/drawLayersRule", [
			["int", "/TXRenderImage_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXRenderImage_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/drawLayersRule/useExtMod", 0],
			["float", "/TXRenderImage_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXRenderImage_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXRenderImage_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXRenderImage_1/renderContinuosly", [
			["bool_int", "/TXRenderImage_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXRenderImage_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/renderContinuosly/useExtMod", 0],
			["float", "/TXRenderImage_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXRenderImage_1/renderNow", [
			["bool_int", "/TXRenderImage_1/renderNow/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/renderNow/useExtMod", 0],
			["float", "/TXRenderImage_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXRenderImage_1/renderWidthHeightRule", 0, 0, 11, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]],
		["int", "/TXRenderImage_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXRenderImage_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXRenderImage_1/clearBeforeRender", [
			["bool_int", "/TXRenderImage_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXRenderImage_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXRenderImage_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXRenderImage_1/clearNow", [
			["bool_int", "/TXRenderImage_1/clearNow/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/clearNow/useExtMod", 0],
			["float", "/TXRenderImage_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/clearColorHue", [
			["float", "/TXRenderImage_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXRenderImage_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/clearColorHue/useExtMod", 0],
			["float", "/TXRenderImage_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/clearColorSaturation", [
			["float", "/TXRenderImage_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXRenderImage_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXRenderImage_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/clearColorBrightness", [
			["float", "/TXRenderImage_1/clearColorBrightness/fixedValue", 0.0, 0, 1],
			["float", "/TXRenderImage_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXRenderImage_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/clearColorAlpha", [
			["float", "/TXRenderImage_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXRenderImage_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXRenderImage_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXRenderImage_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXRenderImage_1/alpha", [
			["float", "/TXRenderImage_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXRenderImage_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/alpha/useExtMod", 0],
			["float", "/TXRenderImage_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/alpha/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/drawPosX", [
			["float", "/TXRenderImage_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXRenderImage_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/drawPosX/useExtMod", 0],
			["float", "/TXRenderImage_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/drawPosY", [
			["float", "/TXRenderImage_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXRenderImage_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/drawPosY/useExtMod", 0],
			["float", "/TXRenderImage_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/drawWidth", [
			["float", "/TXRenderImage_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXRenderImage_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/drawWidth/useExtMod", 0],
			["float", "/TXRenderImage_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/drawHeight", [
			["float", "/TXRenderImage_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXRenderImage_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/drawHeight/useExtMod", 0],
			["float", "/TXRenderImage_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/anchorX", [
			["float", "/TXRenderImage_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXRenderImage_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/anchorX/useExtMod", 0],
			["float", "/TXRenderImage_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/anchorY", [
			["float", "/TXRenderImage_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXRenderImage_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/anchorY/useExtMod", 0],
			["float", "/TXRenderImage_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXRenderImage_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/rotate", [
			["float", "/TXRenderImage_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXRenderImage_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/rotate/useExtMod", 0],
			["float", "/TXRenderImage_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/rotate/softMin", -360, -360, 360],
			["float", "/TXRenderImage_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/rotateMultiply", [
			["float", "/TXRenderImage_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXRenderImage_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/rotateMultiply/useExtMod", 0],
			["float", "/TXRenderImage_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXRenderImage_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/scaleX", [
			["float", "/TXRenderImage_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXRenderImage_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/scaleX/useExtMod", 0],
			["float", "/TXRenderImage_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXRenderImage_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXRenderImage_1/scaleY", [
			["float", "/TXRenderImage_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXRenderImage_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXRenderImage_1/scaleY/useExtMod", 0],
			["float", "/TXRenderImage_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXRenderImage_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXRenderImage_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXRenderImage_1/useScaleXForScaleY", 0],
		[ "modParameterGroupBool", "/TXRenderImage_1/recordVideo", [
			["bool_int", "/TXRenderImage_1/recordVideo/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/recordVideo/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/recordVideo/useExtMod", 0],
			["float", "/TXRenderImage_1/recordVideo/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXRenderImage_1/pauseRecordVideo", [
			["bool_int", "/TXRenderImage_1/pauseRecordVideo/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/pauseRecordVideo/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/pauseRecordVideo/useExtMod", 0],
			["float", "/TXRenderImage_1/pauseRecordVideo/extModValue", 0, 0, 1],
		]],
		["float", "/TXRenderImage_1/maxVideoRecordTimeMinutes", 0, 0, 600],
		["string", "/TXRenderImage_1/recordVideoFolder", "/Users/XXXUserName/Movies/TXV_Movies"],
		["string", "/TXRenderImage_1/recordVideoFileNamePrefix", "TXV_Movie_"],
		["int", "/TXRenderImage_1/recordVideoCodec", 0, 0, 10, ["mpeg4 - uses VideoBitRate", "ProRes LT", "ProRes Standard", "ProRes HQ", "ProRes 4444", "ProRes 4444XQ", "raw video", "h264 - uses VideoBitRate & SpeedPreset", "h264 CRF - uses ConstantRateFactor & speedPreset", "h265 - uses VideoBitRate & SpeedPreset", "h265 CRF - uses ConstantRateFactor & speedPreset", ]],
		// Removed for now
		// ["int", "/TXRenderImage_1/recordVideoFileType", 0, 0, 2, ["mov", "mp4", "avi",]],
		["int", "/TXRenderImage_1/recordVideoBitrate", 6, 0, 30, ["400k","800k", "1 Mbps - Youtube: 360p (SD) 480 x 360  24/25/30 fps", "1.2 Mbps", "1.5 Mbps - Youtube: 360p (SD) 480 x 360  48/50/60 fps", "2 Mbps", "2.5 Mbps - Youtube: 480p (SD) 858 x 480, 24/25/30 fps", "3 Mbps", "4 Mbps - Youtube: 480p (SD) 858 x 480, 48/50/60 fps", "5 Mbps - Youtube: 720p (Half HD)1280 x 720, 24/25/30 fps", "6 Mbps", "7 Mbps - Youtube: 720p (Half HD)1280 x 720, 48/50/60 fps", "8 Mbps - Youtube: 1080p (Full HD) 1920 x 1080, 24/25/30 fps", "10 Mbps", "12 Mbps - Youtube: 1080p (Full HD) 1920 x 1080, 48/50/60 fps", "14 Mbps","16 Mbps - Youtube: 1440p (2k) 2560 x 1440, 24/25/30 fps", "18 Mbps", "20 Mbps", "24 Mbps - Youtube: 1440p (2k) 2560 x 1440 48/50/60 fps", "30 Mbps", "35 Mbps", "40 Mbps - Youtube: 2160p (4k) 3840 x 2160, 24/25/30 fps", "45 Mbps", "50 Mbps", "55 Mbps", "60 Mbps - Youtube: 2160p (4k) 3840 x 2160, 48/50/60 fps", "70 Mbps", "80 Mbps", "90 Mbps", "100 Mbps", ]],
		["int", "/TXRenderImage_1/recordVideoConstantRateFactor", 23, 0, 51],
		["int", "/TXRenderImage_1/recordVideoH6245SpeedPreset", 3, 0, 6, ["very slow", "slower", "slow", "medium (default)", "fast", "faster", "veryfast",]],
/* REMOVED AUDIO FOR NOW
		["bool_int", "/TXRenderImage_1/recordAudioWithVideo", 0],
		["int", "/TXRenderImage_1/recordAudioBitrate", 3, 0, 4 , [ "128k", "160k", "192k", "256k", "320k"]],
		["int", "/TXRenderImage_1/recordAudioDeviceID", 0, 0, 20],
*/
		[ "modParameterGroupBool", "/TXRenderImage_1/saveImageNow", [
			["bool_int", "/TXRenderImage_1/saveImageNow/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/saveImageNow/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/saveImageNow/useExtMod", 0],
			["float", "/TXRenderImage_1/saveImageNow/extModValue", 0, 0, 1],
		]],
		["string", "/TXRenderImage_1/saveImageFolder", "/Users/XXXUserName/Pictures/TXV_Images"],
		["string", "/TXRenderImage_1/saveImageFileNamePrefix", "TXV_Image_"],
		["int", "/TXRenderImage_1/saveImageFormat", 1, 0, 2, ["jpg (May Not Work!!)", "png (slow but high res)", "bmp (fast but big)",]],
		[ "modParameterGroupBool", "/TXRenderImage_1/recordImageSeq", [
			["bool_int", "/TXRenderImage_1/recordImageSeq/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/recordImageSeq/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/recordImageSeq/useExtMod", 0],
			["float", "/TXRenderImage_1/recordImageSeq/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXRenderImage_1/pauseRecordImageSeq", [
			["bool_int", "/TXRenderImage_1/pauseRecordImageSeq/fixedValue", 0],
			["bool_int", "/TXRenderImage_1/pauseRecordImageSeq/fixedModMix", 0],
			["bool_int", "/TXRenderImage_1/pauseRecordImageSeq/useExtMod", 0],
			["float", "/TXRenderImage_1/pauseRecordImageSeq/extModValue", 0, 0, 1],
		]],
		["float", "/TXRenderImage_1/maxImageSeqRecordTimeMinutes", 0, 0, 600],
		["string", "/TXRenderImage_1/imageSeqFolder", "/Users/XXXUserName/Movies/TXV_ImageSeqs/TXV_ImageSeqTEST01"],
		["string", "/TXRenderImage_1/imageSeqFileNamePrefix", "TXV_ImageSeqTEST01_"],
		["int", "/TXRenderImage_1/imageSeqFormat", 1, 0, 2, ["jpg (May Not Work!!)", "png (slow but high res)", "bmp (fast but big)",]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXRenderImage_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Save Image", {arg parameterName;
				parameterName.containsi("saveImage")
				;}],
			["Record Video", {arg parameterName;
				parameterName.containsi("recordVideo")
				|| parameterName.containsi("VideoRecord")
				|| parameterName.containsi("recordAudio")
				;}],
			["Record Image Seq", {arg parameterName;
				parameterName.containsi("imageSeq")
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

	getInitialDisplayOption {  // override
		^"Clear & Render$Clear";
	}

}


