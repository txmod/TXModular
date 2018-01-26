// Copyright (C) 2017 Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveTerrainBuilder {
	classvar classData;

	*initClass {
		classData = ();
		classData.windowPosX = 0;
		classData.windowPosY = 600;
		classData.windowWidth = 1260;
		classData.windowHeight = 600;
		classData.autoBuild = 0;
		classData.resolution = 256;
		classData.layerViewIndex = 0;

		classData.wtData = ();
		classData.wtLayerAData = ();
		classData.wtLayerBData = ();
		classData.wtLayerCData = ();
		classData.wtLayerDData = ();

		classData.wtPresetData = ();
		classData.wtLayerPresetData = ();

		classData.guiData = ();
		classData.guiData.alwaysOnTop = 1;

		classData.historyData = ();
		classData.historyData.maxHistorySize = 40;
		classData.historyData.historyArray = [];
		classData.historyData.histIndex = 0;

		classData.holdImage = Image.new(classData.resolution @ classData.resolution);
		classData.terrainArray = Array.fill(classData.resolution * classData.resolution, 0);

		// init
		this.buildFunctionArrays;
		this.loadDefaultData;

		// load settings from disk
		this.loadWTBuildSettings;

		// re-init data & presets & history
		this.loadDefaultData;
		this.saveWTPreset(' Default_WaveTerrain');
		this.saveWTLayerPreset(' Default_Layer', classData.wtLayerAData);
		this.addToHistory;
	}

	*closeWindow {
		if (classData.wtBuilderWindow.notNil, {
			classData.windowPosX = classData.wtBuilderWindow.bounds.left;
			classData.windowPosY = classData.wtBuilderWindow.bounds.top;
			classData.windowWidth = classData.wtBuilderWindow.bounds.width;
			classData.windowHeight = classData.wtBuilderWindow.bounds.height;
			classData.wtBuilderWindow.close;
		});
	}

	*loadDefaultData {
		//init Terrain
		classData.wtData.presetIndex = 0;
		classData.wtData.presetText = "";
		classData.wtData.structureFuncIndex = 0;
		classData.wtData.rangeFuncIndex = 0;
		classData.wtData.xMin = 0;
		classData.wtData.xMax = 1;
		classData.wtData.yMin = 0;
		classData.wtData.yMax = 1;
		classData.wtData.color1H = 0.6;
		classData.wtData.color1S = 0.6;
		classData.wtData.color1B = 0.6;
		classData.wtData.color2H = 0.4;
		classData.wtData.color2S = 0.5;
		classData.wtData.color2B = 0.2;
		classData.wtData.color3H = 0;
		classData.wtData.color3S = 0.9;
		classData.wtData.color3B = 1;
		//
		classData.wtLayerAData = ();
		classData.wtLayerAData.layerPresetIndex = 0;
		classData.wtLayerAData.layerPresetText = "";
		classData.wtLayerAData.layerGenFuncIndex = 0;
		classData.wtLayerAData.genMult = 1;
		classData.wtLayerAData.genXPower = 0;
		classData.wtLayerAData.genYPower = 0;
		classData.wtLayerAData.genOffset = 0;
		classData.wtLayerAData.genXYFuncIndex = 0;
		classData.wtLayerAData.xOffset = 0;
		classData.wtLayerAData.yOffset = 0;
		classData.wtLayerAData.xInvert = 0;
		classData.wtLayerAData.yInvert = 0;
		classData.wtLayerAData.layerPost1Active = 1;
		classData.wtLayerAData.layerPost2Active = 1;
		classData.wtLayerAData.layerPost3Active = 1;
		classData.wtLayerAData.layerPost4Active = 1;
		classData.wtLayerAData.layerPost5Active = 1;
		classData.wtLayerAData.layerPost1FuncIndex = 0;
		classData.wtLayerAData.layerPost2FuncIndex = 0;
		classData.wtLayerAData.layerPost3FuncIndex = 0;
		classData.wtLayerAData.layerPost4FuncIndex = 0;
		classData.wtLayerAData.layerPost5FuncIndex = 0;
		classData.wtLayerAData.layerPost1YVal = 0;
		classData.wtLayerAData.layerPost2YVal = 0;
		classData.wtLayerAData.layerPost3YVal = 0;
		classData.wtLayerAData.layerPost4YVal = 0;
		classData.wtLayerAData.layerPost5YVal = 0;
		classData.wtLayerAData.power = 1;
		classData.wtLayerAData.mult = 1;
		classData.wtLayerAData.multXPower = 0;
		classData.wtLayerAData.multYPower = 0;
		classData.wtLayerAData.offset = 0;
		//
		classData.wtLayerBData = classData.wtLayerAData.deepCopy;
		classData.wtLayerBData.layerJoinFuncIndex = 0;
		//
		classData.wtLayerCData = classData.wtLayerBData.deepCopy;
		classData.wtLayerDData = classData.wtLayerBData.deepCopy;
	}

	//////////////////////// Build settings

	*saveWTBuildSettings {
		var holdFile, holdFileData, holdPath, holdPath2;

		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular/TXWaveTerrainBuildSettings.tx");

		holdFileData = ["TXModSystemWaveTerrainSettingsData",
			classData.wtData.getPairs, classData.wtLayerAData.getPairs, classData.wtLayerBData.getPairs,
			classData.wtLayerCData.getPairs, classData.wtLayerDData.getPairs,
			classData.wtPresetData.getPairs, classData.wtLayerPresetData.getPairs,
		];
		holdFile = File(holdPath.fullPath, "w");
		if (File.exists(holdPath.fullPath)) {
			holdFile.write("#" ++  holdFileData.asCompileString ++ "\n");
			//"Saving TXWaveTerrainBuildSettings.tx".postln;
		} {
			"".postln;
			"TX Warning: TXWaveTerrainBuilder.saveWTBuildSettings: File doesn't exist: ".postln;
			holdPath.postln;
		};
		holdFile.close;
	}

	*loadWTBuildSettings {
		var holdPath, holdFile, holdFileData;
		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular");
		holdFile = PathName.new(Platform.userAppSupportDir +/+ "TXModular" +/+ "TXWaveTerrainBuildSettings.tx");
		// if directory doesn't exist, create it.
		if (holdPath.isFolder.not, {
			// NEW CODE
			holdPath.fullPath.makeDir;
		});
		if (File.exists(holdFile.fullPath),  {
			// if file exists, update values.
			holdFileData = thisProcess.interpreter.executeFile(holdFile.fullPath);
			if (holdFileData.class == Array, {
				classData.wtData.putPairs(holdFileData[1]);
				classData.wtLayerAData.putPairs(holdFileData[2]);
				classData.wtLayerBData.putPairs(holdFileData[3]);
				classData.wtLayerCData.putPairs(holdFileData[4]);
				classData.wtLayerDData.putPairs(holdFileData[5]);
				classData.wtPresetData.putPairs(holdFileData[6]);
				classData.wtLayerPresetData.putPairs(holdFileData[7]);
				//"Loading TXWaveTerrainBuildSettings.tx".postln;
			});
		});
	}

	*deleteWTBuildSettings {
		var holdPath, holdFile, holdFileData;
		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular");
		holdFile = PathName.new(Platform.userAppSupportDir +/+ "TXModular" +/+ "TXWaveTerrainBuildSettings.tx");
		if (File.exists(holdFile.fullPath),  {
			// if file exists, delete it.
			File.delete(holdFile.fullPath);
		});
	}

	//////////////////////// presets

	// *makePresetName {arg text;
	// 	(classData.wtLayerPresetData.keys.size + 1).asString.padLeft(5, "0") ++ " " ++  text;
	// };

	*getWTPresetNames {
		var outArray = classData.wtPresetData.keys.asArray.collect({arg item; item.asString}).sort;
		^outArray;
	}

	*getWTPresetData {
		var outData = [classData.wtData.getPairs, classData.wtLayerAData.getPairs, classData.wtLayerBData.getPairs,
			classData.wtLayerCData.getPairs, classData.wtLayerDData.getPairs,];
		^outData;
	}

	*loadWTPresetData {arg holdFileData;
		var holdTerrainData;
		if (holdFileData.class == Array, {
			holdTerrainData = ();
			holdTerrainData.putPairs(holdFileData[0]);
			holdTerrainData.presetIndex = nil; // remove  from data
			holdTerrainData.presetText = nil; // remove  from data
			classData.wtData.putAll(holdTerrainData);
			// layers
			[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData
			].do({arg wtLayerData, i;
				var tempLayerData = ();
				tempLayerData.putPairs(holdFileData[i + 1]);
				tempLayerData.layerPresetIndex = 0; // remove  from data
				tempLayerData.layerPresetText = ""; // remove  from data
				wtLayerData.putAll(tempLayerData);
			});
		});
	}

	// Drag Data
	*getWTPresetDragData {
		var outData = [\TXWaveTerrainPresetData, this.getWTPresetData, classData.terrainArray];
		^outData;
	}

	// Drop Data
	*loadWTPresetDropData {arg dropData;
		if ((dropData.class == Array) and: {dropData.asArray[0] == \TXWaveTerrainPresetData}, {
			this.loadWTPresetData(dropData[1]);
			classData.terrainArray = dropData[2];
			this.addToHistory;
			this.showWindow;
			this.rebuildWT;
		});
	}

	*loadWTPreset {arg presetName;
		if (classData.wtPresetData[presetName.asSymbol].notNil, {
			//("Loading Wave Terrain Preset " ++ presetName).postln;
			this.loadWTPresetData(classData.wtPresetData[presetName.asSymbol]);
			this.addToHistory;
		});
	}

	*saveWTPreset {arg presetName;
		classData.wtPresetData[presetName.asSymbol] = this.getWTPresetData;
		// save wt settings
		this.saveWTBuildSettings;
	}


	*getWTLayerPresetNames {
		var outArray = classData.wtLayerPresetData.keys.asArray.collect({arg item; item.asString}).sort;
		^outArray;
	}

	*saveWTLayerPreset {arg presetName, layerData;
		if (layerData.notNil, {
			classData.wtLayerPresetData[presetName.asSymbol] = layerData.getPairs;
		}, {
			classData.wtLayerPresetData[presetName.asSymbol] = nil;
		});
		// save wt settings
		this.saveWTBuildSettings;
	}

	*loadWTLayerPreset {arg presetName, layerData;
		var tempLayerData = ();
		if (classData.wtLayerPresetData[presetName.asSymbol].notNil, {
			//("Loading Wave Terrain Layer Preset " ++ presetName).postln;
			layerData.putPairs(classData.wtLayerPresetData[presetName.asSymbol]);
			tempLayerData.putPairs(classData.wtLayerPresetData[presetName.asSymbol]);
			tempLayerData.layerJoinFuncIndex = nil; // remove  from data
			tempLayerData.layerPresetIndex = nil; // remove  from data
			tempLayerData.layerPresetText = nil; // remove  from data
			layerData.putAll(tempLayerData);
			this.addToHistory;
		});
	}


	///////////////////// History

	*addToHistory {
		var newData = [classData.wtData.getPairs, classData.wtLayerAData.getPairs, classData.wtLayerBData.getPairs,
			classData.wtLayerCData.getPairs, classData.wtLayerDData.getPairs];
		// if data is different add to history
		if (classData.historyData.historyArray[classData.historyData.histIndex] != newData, {
			// truncate hist at current index
			classData.historyData.historyArray = classData.historyData.historyArray.keep(classData.historyData.histIndex + 1);
			classData.historyData.historyArray = classData.historyData.historyArray.add(newData);
			classData.historyData.historyArray =
			classData.historyData.historyArray.keep(0 - classData.historyData.maxHistorySize); // limit size
			classData.historyData.histIndex = classData.historyData.historyArray.size - 1;
			this.coloriseHistoryButtons;
		});
	}

	*loadHistoryData {
		var holdData = classData.historyData.historyArray[classData.historyData.histIndex];
		[classData.wtData, classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData
		].do({arg item, i;
			var tempEvent;
			if (i == 0, {
				tempEvent = ();
				tempEvent.putPairs(holdData[i]);
				item.putAll(tempEvent);
			}, {
				item.putPairs(holdData[i]);
			});
		});
	}

	*loadHistoryPrev {
		if (classData.historyData.histIndex > 0, {
			classData.historyData.histIndex = classData.historyData.histIndex - 1;
			this.loadHistoryData;
			this.checkAutoBuild;
			this.coloriseHistoryButtons;
		});
	}

	*loadHistoryNext {
		if (classData.historyData.histIndex < (classData.historyData.historyArray.size - 1), {
			classData.historyData.histIndex = classData.historyData.histIndex + 1;
			this.loadHistoryData;
			this.checkAutoBuild;
			this.coloriseHistoryButtons;
		});
	}

	//////////////////////// Terrain Building

	*buildLayer {arg x, y, aData;
		var outLayer;
		if (aData.xInvert == 1, {
			x = 1 - x;
		});
		if (aData.yInvert == 1, {
			y = 1 - y;
		});
		outLayer = (
			classData.arrLayerPostFuncs[aData.layerPost5Active * aData.layerPost5FuncIndex][1].value(
				classData.arrLayerPostFuncs[aData.layerPost4Active * aData.layerPost4FuncIndex][1].value(
					classData.arrLayerPostFuncs[aData.layerPost3Active * aData.layerPost3FuncIndex][1].value(
						classData.arrLayerPostFuncs[aData.layerPost2Active * aData.layerPost2FuncIndex][1].value(
							classData.arrLayerPostFuncs[aData.layerPost1Active * aData.layerPost1FuncIndex][1].value(
								classData.arrLayerGenFuncs[aData.layerGenFuncIndex][1].value(x, y, aData), aData.layerPost1YVal
							), aData.layerPost2YVal
						), aData.layerPost3YVal
					), aData.layerPost4YVal
				), aData.layerPost5YVal
			)
			** aData.power
			* aData.mult
			* ((x + aData.xOffset) ** aData.multXPower)
			* ((y + aData.yOffset) ** aData.multYPower)
		) + aData.offset;
		^outLayer;
	}

	*updateTerrain {
		var holdWidth, holdHeight, holdSize;
		var terrainFunc, rangeFunc;
		terrainFunc = classData.arrStructureFuncs[classData.wtData.structureFuncIndex][1];
		rangeFunc = classData.arrRangeFuncs[classData.wtData.rangeFuncIndex][1];
		holdWidth = holdHeight = classData.resolution;
		holdSize = holdWidth * holdHeight;
		// build terrain array
		classData.terrainArray = Array.fill(holdSize, {arg i;
			var xnow, ynow, x, y, z, holdVal, percent;
			if ((i % (holdSize * 0.1).asInteger) == 0, {
				percent = ((i * 100) / holdSize).round;
			});
			xnow = i%holdWidth;
			ynow = (i-xnow).div(holdWidth);
			x = (xnow/holdWidth).linlin(0, 1, classData.wtData.xMin, classData.wtData.xMax);
			y = (ynow/holdHeight).linlin(0, 1, classData.wtData.yMin, classData.wtData.yMax);
			holdVal = terrainFunc.value(x, y);
			if (holdVal.isNaN or: {holdVal.isNumber.not} or: {holdVal == inf} or: {holdVal == -inf}, {
				holdVal = 0;
			});
			holdVal;
		});
		// adjust range
		classData.terrainArray = rangeFunc.value(classData.terrainArray);
	}

	// image

	*updateImage {
		var holdVal, holdColor, colA, colB, colC;
		if (classData.holdImage.width != classData.resolution, {
			classData.holdImage = Image.new(classData.resolution @ classData.resolution);
		});
		// add data to image with color
		colA = Color.hsv(classData.wtData.color1H.min(0.99), classData.wtData.color1S, classData.wtData.color1B);
		colB = Color.hsv(classData.wtData.color2H.min(0.99), classData.wtData.color2S, classData.wtData.color2B);
		colC = Color.hsv(classData.wtData.color3H.min(0.99), classData.wtData.color3S, classData.wtData.color3B);
		classData.holdImage.pixels_(
			Int32Array.fill(classData.resolution * classData.resolution, {arg i;
				holdVal = classData.terrainArray[i];
				holdColor = this.getColorFromVal(holdVal, colA, colB, colC);
				this.getRGBAToInt(
					(holdColor.red * 255).round(1).asInteger,
					(holdColor.green * 255).round(1).asInteger,
					(holdColor.blue * 255).round(1).asInteger,
					255
				);
			})
		);
	}

	*getColorFromVal {arg val, colA, colB, colC;
		var outColour;
		// value range -1 to 1
		if (val < 0, {
			outColour = colA.blend(colB, val + 1);
		}, {
			outColour = colB.blend(colC, val);
		});
		^outColour;
	}

	*getRGBAToInt {arg r, g, b, a;
		^((a<<24) | (r<<16) | (g<<8) | b);
	}

	///////////////////////////////////
	*buildFunctionArrays {  // function choice arrays

		classData.arrRangeFuncs = [
			["Normalise - bipolar", {arg floatArray; floatArray.normalize2;}],
			[" Normalise ", {arg floatArray; floatArray.normalize(-1.0, 1.0);}],
			["Fold", {arg floatArray; floatArray.fold(-1.0, 1.0);}],
			["Wrap", {arg floatArray; floatArray.wrap(-1.0, 1.0);}],
			["Clip", {arg floatArray; floatArray.clip(-1.0, 1.0);}],
		];

		classData.arrStructureFuncs = [
			/* 0 */ ["1L (A)", {arg x,y; this.buildLayer(x,y, classData.wtLayerAData);}],
			/* 1 */ ["1L (B)", {arg x,y; this.buildLayer(x,y, classData.wtLayerBData);}],
			/* 2 */ ["1L (C)",{arg x,y; this.buildLayer(x,y, classData.wtLayerCData);}],
			/* 3 */ ["1L (D)",{arg x,y; this.buildLayer(x,y, classData.wtLayerDData);}],
			/* 4 */ ["2L (A:B)", {arg x,y;
				var layA, layB, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, layB);
			}],
			/* 5*/ ["3L ((A:B) : C)", {arg x,y;
				var layA, layB, layC, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, layB);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(chain, layC);
			}],
			/* 6*/ ["3L (A : (B:C))", {arg x,y;
				var layA, layB, layC, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(layB, layC);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, chain);
			}],
			/* 7*/ ["4L (((A:B) : C) : D)", {arg x,y;
				var layA, layB, layC, layD, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				layD = this.buildLayer(x,y, classData.wtLayerDData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, layB);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(chain, layC);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerDData.layerJoinFuncIndex][1].value(chain, layD);
			}],
			/* 8*/ ["4L ((A:B) : (C:D))", {arg x,y;
				var layA, layB, layC, layD, chainAB, chainCD, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				layD = this.buildLayer(x,y, classData.wtLayerDData);
				chainAB = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, layB);
				chainCD = classData.arrBinaryOpFuncs[classData.wtLayerDData.layerJoinFuncIndex][1].value(layC, layD);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(chainAB, chainCD);
			}],
			/* 9*/ ["4L ((A : (B:C)) : D)", {arg x,y;
				var layA, layB, layC, layD, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				layD = this.buildLayer(x,y, classData.wtLayerDData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(layB, layC);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, chain);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerDData.layerJoinFuncIndex][1].value(chain, layD);
			}],
			/* 10*/ ["4L (A : (B : (C:D)))", {arg x,y;
				var layA, layB, layC, layD, chain;
				layA = this.buildLayer(x,y, classData.wtLayerAData);
				layB = this.buildLayer(x,y, classData.wtLayerBData);
				layC = this.buildLayer(x,y, classData.wtLayerCData);
				layD = this.buildLayer(x,y, classData.wtLayerDData);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerDData.layerJoinFuncIndex][1].value(layC, layD);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerCData.layerJoinFuncIndex][1].value(layB, chain);
				chain = classData.arrBinaryOpFuncs[classData.wtLayerBData.layerJoinFuncIndex][1].value(layA, chain);
			}],
		];

		classData.arrBinaryOpFuncs = [
			// binary operations ==========
			["x + y", {arg x, y; x + y}],
			["x - y", {arg x, y; x - y}],
			["y - x", {arg x, y; y - x}],
			["x * y", {arg x, y; x * y}],
			["x / y", {arg x, y; x / y}],
			["y / x", {arg x, y; y / x}],
			["x % y", {arg x, y; x % y}],
			["y % x", {arg x, y; y % x}],
			["x ** y", {arg x, y; x ** y}],
			["y ** x", {arg x, y; y ** x}],
			["round (x, y)", {arg x, y; round (x, y)}],
			["round (y, x)", {arg x, y; round (y, x)}],
			["roundUp (x, y)", {arg x, y; roundUp (x, y)}],
			["roundUp (y, x)", {arg x, y; roundUp (y, x)}],
			["thresh (x, y)", {arg x, y; thresh (x, y)}],
			["thresh (y, x)", {arg x, y; thresh (y, x)}],
			["min (x, y)", {arg x, y; min (x, y)}],
			["max (x, y)", {arg x, y; max (x, y)}],
			["fold2 (x, y)", {arg x, y; fold2 (x, y)}],
			["fold2 (y, x)", {arg x, y; fold2 (y, x)}],
			["wrap2 (x, y)", {arg x, y; wrap2 (x, y)}],
			["wrap2 (y, x)", {arg x, y; wrap2 (y, x)}],
			["rrand (x, y)", {arg x, y; rrand (x, y)}],
			["exprand (x, y)", {arg x, y; exprand (x, y)}],
			["trunc (x, y)", {arg x, y; trunc (x, y)}],
			["trunc (y, x)", {arg x, y; trunc (y, x)}],
			["hypot (x, y)", {arg x, y; hypot (x, y)}],
			["atan2 (x, y)", {arg x, y; atan2 (x, y)}],
			["atan2 (y, x)", {arg x, y; atan2 (y, x)}],
			["(x == y).asInteger", {arg x, y; (x == y).asInteger}],
			["(x != y).asInteger", {arg x, y; (x != y).asInteger}],
			["(x > y).asInteger", {arg x, y; (x > y).asInteger}],
			["(x < y).asInteger", {arg x, y; (x < y).asInteger}],
			["(x >= y).asInteger", {arg x, y; (x >= y).asInteger}],
			["(x <= y).asInteger", {arg x, y; (x <= y).asInteger}],
		];

		classData.arrLayerPostFuncs = [
			["(x) [no effect]", {arg input; input; }],
			["neg(x)", {arg input; neg(input); }],
			["abs(x)", {arg input; abs(input); }],
			["sign(x)", {arg input; sign(input); }],
			["sin(x)", {arg input; sin(input); }],
			["cos(x)", {arg input; cos(input); }],
			["tan(x)", {arg input; tan(input); }],
			["asin(x)", {arg input; asin(input); }],
			["acos(x)", {arg input; acos(input); }],
			["atan(x)", {arg input; atan(input); }],
			["sinh(x)", {arg input; sinh(input); }],
			["cosh(x)", {arg input; cosh(input); }],
			["tanh(x)", {arg input; tanh(input); }],
			["frac(x)", {arg input; frac(input); }],
			["ceil(x)", {arg input; ceil(input); }],
			["floor(x)", {arg input; floor(input); }],
			["squared(x)", {arg input; squared(input); }],
			["cubed(x)", {arg input; cubed(input); }],
			["sqrt(x)", {arg input; sqrt(input); }],
			["exp(x)", {arg input; exp(input); }],
			["reciprocal(x)", {arg input; reciprocal(input); }],
			["rand(x)", {arg input; rand(input); }],
			["rand2(x)", {arg input; rand2(input); }],
			["linrand(x)", {arg input; linrand(input); }],
			["bilinrand(x)", {arg input; bilinrand(input); }],
			["xrand2(x)", {arg input; xrand2(input); }],
			["log(x)", {arg input; input.log; }],
			["log2(x)", {arg input; input.log2; }],
			["log10(x)", {arg input; input.log10; }],
		]
		++ classData.arrBinaryOpFuncs;


		classData.arrLayerGenFuncs = [
			["value ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["sin ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; sin (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["cos ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; cos (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["tan ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; tan (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["asin ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; asin (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["acos ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; acos (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["atan ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; atan (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["sinh ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; sinh (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["cosh ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; cosh (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["tanh ((2*pi * genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; tanh (2*pi * lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["rand ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; rand (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["rand2 ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; rand2 (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["linrand ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; linrand (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["bilinrand ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; bilinrand (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["sum3rand ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; sum3rand (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["neg ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; neg (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["abs ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; abs (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["sign ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; sign (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["frac ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; frac (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["ceil ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; ceil (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["floor ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; floor (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["squared ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; squared (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["cubed ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; cubed (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["sqrt ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; sqrt (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["exp ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; exp (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["reciprocal ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; reciprocal (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["log ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; log (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["log2 ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; log2 (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			["log10 ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
				{arg x, y, lay; log10 (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
					((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
			//
			// TEMPLATE:
			// ["XXX ((genMult * XYFunc((x**genXPower), (y**genYPower))) + genOffset)",
			// {arg x, y, lay; XXX (lay.genMult * classData.arrBinaryOpFuncs[lay.genXYFuncIndex][1].value(
			// ((x + lay.xOffset) ** lay.genXPower), ((y + lay.yOffset) ** lay.genYPower)) + lay.genOffset); }],
		];
	}

	//////////////////////// GUI

	*showImageWindow {
		var holdSize = classData.resolution * 2;
		// create window if needed
		if (classData.imageWindow.isNil, {
			classData.imageWindow = Window.new(
				"Wave Terrain Image",
				Rect(1250, 500, holdSize, holdSize),
				resizable: true, scroll: true
			);
			classData.imageWindow.onClose_({
				classData.imageWindow = nil;
			});
			classData.imageWindow.alwaysOnTop_(true);
		});
		classData.imageWindow.drawFunc_({arg view;
			classData.holdImage.drawInRect(
				Rect(0, 0, classData.imageWindow.bounds.width, classData.imageWindow.bounds.height), nil);
		});
		classData.imageWindow.front;
		classData.imageWindow.refresh;
	}

	*showWindow {
		var holdView;
		var holdArrDisplayLayers, holdArrParms;
		var arrWTPresetNames, arrWTLayerPresetNames;
		var holdFont, holdString, holdPopup, holdPresetNameView, holdPresetPopup;
		var holdWTPresetSaveBtn, holdWidth, holdHeight, holdScreenRect;

		// init
		holdScreenRect = Window.availableBounds;
		holdWidth =  classData.windowWidth.min(holdScreenRect.width);
		holdHeight = classData.windowHeight.min(holdScreenRect.height);
		// holdWidth =  classData.windowWidth;
		// holdHeight = classData.windowHeight;
		arrWTPresetNames = classData.wtPresetData.keys.asString;
		arrWTLayerPresetNames = classData.wtLayerPresetData.keys.asString;

		// Set Default Font
		holdFont = Font.default;
		Font.default = Font("Helvetica", 12);

		// create image if needed
		if (classData.holdImage.isNil, {
			classData.holdImage = Image.new(classData.resolution @ classData.resolution);
			// init
			classData.holdImage.interpolation_(\smooth); // Best
			//classData.holdImage.interpolation_(\fast);
		});

		// create window if needed
		if (classData.wtBuilderWindow.isNil, {
			classData.wtBuilderWindow = Window.new(
				"Wave Terrain Builder",
				Rect(classData.windowPosX, classData.windowPosY, holdWidth, holdHeight),
				//Rect(0, 400, 1250, 940),
				resizable: true, scroll: true
			).background_(TXSystem1.dataBank.windowColour);
		}, {
			// else store visibleOrigin
			classData.guiData.visibleOrigin = classData.wtBuilderWindow.view.visibleOrigin;
		});
		classData.wtBuilderWindow.front;

		// set visibleOrigin
		if (classData.guiData.visibleOrigin.notNil, {
			{
				classData.wtBuilderWindow.view.visibleOrigin = classData.guiData.visibleOrigin;
			}.defer(0.05);
		});

		classData.wtBuilderWindow.onClose_({
			classData.wtBuilderWindow = nil;
			if (classData.holdImage.notNil, {
				classData.holdImage.free;
				classData.holdImage = nil;
			});
			if (classData.imageWindow.notNil, {classData.imageWindow.close});
		});
		classData.wtBuilderWindow.alwaysOnTop = classData.guiData.alwaysOnTop.asBoolean;

		if (classData.guiData.viewBox.notNil, {
			TXSystem1GUI.deferRemoveView(classData.guiData.viewBox);
		});

		// make box to display
		classData.guiData.viewBox = CompositeView(classData.wtBuilderWindow, Rect(0, 0, holdWidth, 900));
		if (classData.guiData.isBuilding == true, {
			classData.guiData.viewBox.background_(TXColor.orange(0.5));
		}, {
			classData.guiData.viewBox.background_(TXColour.grey(0.85).blend(TXColour.sysGuiCol1, 0.2));
		});
		classData.guiData.viewBox.decorator = FlowLayout(classData.guiData.viewBox.bounds);

		// next line
		classData.guiData.viewBox.decorator.nextLine;

		// widgets

		// History buttons
		classData.guiData.histPrevBtn = Button(classData.guiData.viewBox, 18 @ 24)
		.states_([["<", TXColor.yellow, TXColor.sysGuiCol1]])
		.action_({
			this.loadHistoryPrev;
			this.showWindow;
		});
		classData.guiData.histNextBtn = Button(classData.guiData.viewBox, 18 @ 24)
		.states_([[">", TXColor.yellow, TXColor.sysGuiCol1]])
		.action_({
			this.loadHistoryNext;
			this.showWindow;
		});
		this.coloriseHistoryButtons;

		classData.guiData.viewBox.decorator.shift(4,0);

		// Help button
		Button(classData.guiData.viewBox, 40 @ 24)
		.states_([["Help", TXColor.white, TXColor.sysHelpCol]])
		.action_({ TXHelpScreen.openFile("TX_Wave Terrain Builder")});
		classData.guiData.viewBox.decorator.shift(2,0);


		classData.guiData.viewBox.decorator.shift(30,0);

		// Build buttons
		classData.guiData.buildButton = Button(classData.guiData.viewBox, 80 @ 24)
		.states_([["Build Terrain", TXColor.white, TXColor.sysGuiCol3]])
		.action_({this.rebuildWT;});

		TXCheckBox(classData.guiData.viewBox, 90 @ 24, "Auto-Build", TXColor.sysGuiCol3,
			TXColor.white, TXColor.white, TXColor.sysGuiCol3)
		.value_(classData.autoBuild)
		.action_({arg view;
			classData.autoBuild = view.value;
			this.checkAutoBuild;
		});
		classData.guiData.viewBox.decorator.shift(30,0);

		classData.guiData.dragView = DragBoth(classData.guiData.viewBox, 80 @ 24);
		classData.guiData.dragView.setBoth = false;
		classData.guiData.dragView.string = "drag & drop";
		classData.guiData.dragView.align = \center;
		classData.guiData.dragView.background_(TXColor.paleYellow);
		classData.guiData.dragView.stringColor_(TXColor.sysGuiCol1);
		classData.guiData.dragView.canReceiveDragHandler = {View.currentDrag.isKindOf(Array);};
		classData.guiData.dragView.receiveDragHandler = {arg view;
			this.loadWTPresetDropData(View.currentDrag.value);
		};
		classData.guiData.dragView.object = nil;
		classData.guiData.dragView.dragLabel = "Wave Terrain";
		classData.guiData.dragView.beginDragAction = {arg view;
			this.getWTPresetDragData;
		};

		classData.guiData.viewBox.decorator.shift(30,0);

		holdView = TXCheckBox(classData.guiData.viewBox, Rect(0, 0, 120, 24), "window on top", TXColor.sysGuiCol2,
			TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value =  classData.guiData.alwaysOnTop;
		holdView.action = {|view|
			classData.guiData.alwaysOnTop = view.value;
			classData.wtBuilderWindow.alwaysOnTop = classData.guiData.alwaysOnTop.asBoolean;
		};

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 4);
		// line
		UserView(classData.guiData.viewBox,
		(classData.guiData.viewBox.bounds.width - 12) @ 2)
		.background_(TXColor.grey(0.95));

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 6);

		// WT Presets
		holdPresetPopup = TXPopupPlusMinus(classData.guiData.viewBox, 500 @ 20, "Wave Terrain presets",
			this.getWTPresetNames, {arg view; classData.wtData.presetIndex = view.value;},
			classData.wtData.presetIndex, labelWidth:120);
		Button(classData.guiData.viewBox, 60 @ 20)
		.states_([["Load", TXColor.white, TXColor.sysGuiCol1]])
		.action_({arg view;
			if (classData.wtPresetData.keys.size > 0, {
				classData.wtData.presetText = this.getWTPresetNames[classData.wtData.presetIndex].asString;
				this.loadWTPreset(this.getWTPresetNames[classData.wtData.presetIndex]);
				this.checkAutoBuild;
				this.showWindow;
			});
		});
		classData.guiData.viewBox.decorator.shift(9,0);
		// Button(classData.guiData.viewBox, 70 @ 20)
		// .states_([["Overwrite", TXColor.white, TXColor.sysGuiCol2]])
		// .action_({
		// 	if (classData.wtPresetData.keys.size > 0, {
		// 		this.saveWTPreset(this.getWTPresetNames[classData.wtData.presetIndex]);
		// 	});
		// });
		Button(classData.guiData.viewBox, 50 @ 20)
		.states_([["Delete", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			if (classData.wtPresetData.keys.size > 0, {
				this.saveWTPreset(this.getWTPresetNames[classData.wtData.presetIndex], nil);
				classData.wtData.presetIndex = 0;
				this.showWindow;
			});
		});
		classData.guiData.viewBox.decorator.shift(9,0);
		// StaticText
		StaticText(classData.guiData.viewBox, 60 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.string_("Name").align_(\right);
		// Name field
		holdPresetNameView = TextField(classData.guiData.viewBox, 300 @ 20)
		.string_(classData.wtData.presetText)
		.action_({arg view;
			var holdIndex, holdString;
			holdString = this.removeInitSpaces(view.string);
			view.string = holdString;
			classData.wtData.presetText = holdString;
			holdIndex = this.getWTPresetNames.indexOfEqual(holdString);
			if (holdIndex.notNil, {
				classData.wtData.presetIndex = holdIndex;
				holdPresetPopup.value_(holdIndex);
				holdWTPresetSaveBtn.states_([["Overwrite", TXColor.white, TXColor.sysGuiCol1]]);
			}, {
				holdWTPresetSaveBtn.states_([["Save", TXColor.white, TXColor.sysGuiCol2]]);
			});
			if (holdString == "", {
				holdWTPresetSaveBtn.visible = false;
			}, {
				holdWTPresetSaveBtn.visible = true;
			});
		});

		Button(classData.guiData.viewBox, 18 @ 20)
		.states_([["X", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			holdPresetNameView.string = "";
			classData.wtData.presetText = "";
			holdWTPresetSaveBtn.visible = false;
		});
		holdWTPresetSaveBtn = Button(classData.guiData.viewBox, 60 @ 20)
		.action_({
			var holdString;
			if (classData.wtPresetData.keys.size > 0, {
				holdString = holdPresetNameView.string;
				this.saveWTPreset(holdString);
				classData.wtData.presetIndex = this.getWTPresetNames.indexOfEqual(holdString) ? 0;
				classData.wtData.presetText = holdString;
				this.showWindow;
			});
		});
		if (this.getWTPresetNames.indexOfEqual(classData.wtData.presetText).notNil, {
			holdWTPresetSaveBtn.states_([["Overwrite", TXColor.white, TXColor.sysGuiCol1]]);
		}, {
			holdWTPresetSaveBtn.states_([["Save", TXColor.white, TXColor.sysGuiCol2]]);
		});
		if (classData.wtData.presetText == "", {
			holdWTPresetSaveBtn.visible = false;
		}, {
			holdWTPresetSaveBtn.visible = true;
		});

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 2);
		// line
		UserView(classData.guiData.viewBox,
		(classData.guiData.viewBox.bounds.width - 12) @ 2)
		.background_(TXColor.grey(0.95));

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 6);

		[
			[\buttons, \structureFuncIndex, classData.arrStructureFuncs.collect({arg item, i; [item[0], i];}),
				{this.showWindow; this.dataChanged; }, "Layers Structure"],
				[\newLine, 2], // height
				[\buttons, \rangeFuncIndex, classData.arrRangeFuncs.collect({arg item, i; [item[0], i];}),
					{this.dataChanged; this.showWindow;}, "Output range function"],
				[\newLine, 2], // height
		].do({arg arrParms, i;
			this.makeGUIElement(arrParms, classData.wtData);
		});

		// // next line
		// classData.guiData.viewBox.decorator.nextLine.shift(0, 2);
		// // line
		// UserView(classData.guiData.viewBox,
		// (classData.guiData.viewBox.bounds.width - 12) @ 3)
		// .background_(TXColor.grey(0.8));

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 6);

		[
			// [\newLine, 8], // height
			// [\buttons, \resolution, [["128", 128], ["256", 256], ["384", 384], ["512", 512]],
			// 	{classData.holdImage = Image.new(classData.resolution @ classData.resolution);
			// 		{this.dataChanged; this.showWindow}.defer(0.1);
			// 	},
			// "Resolution"],
			//[\newLine, 2], // height
			[\buttons, \layerViewIndex, [["     Global     ", 0], ["     Layer A     ", 1],
				["     Layer B     ", 2], ["     Layer C     ", 3],
				["     Layer D     ", 4], ["     Layers Mix     ", 5],  ],
				{this.showWindow}, "Display", TXColor.sysGuiCol2],
		].do({arg arrParms, i;
			this.makeGUIElement(arrParms, classData);
		});
		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 2);

		// line
		UserView(classData.guiData.viewBox,
			(classData.guiData.viewBox.bounds.width - 12) @ 2)
		.background_(TXColor.grey(0.95));

		// next line
		classData.guiData.viewBox.decorator.nextLine.shift(0, 2);

		// Display global/layer controls
		if (classData.layerViewIndex == 0, {
			[
				[\newLine, 4], // height
				[\label, "X & Y Ranges:", 120], // width
				[\newLine, 4], // height
				[\slider, \xMin, ControlSpec(-10, 10),
					[-4, -3, -2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2, 3, 4]],
				[\newLine, 4], // height
				[\slider, \xMax, ControlSpec(-10, 10),
					[-4, -3, -2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2, 3, 4]],
				[\newLine, 4], // height
				[\slider, \yMin, ControlSpec(-10, 10),
					[-4, -3, -2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2, 3, 4]],
				[\newLine, 4], // height
				[\slider, \yMax, ControlSpec(-10, 10),
					[-4, -3, -2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2, 3, 4]],
			].do({arg arrParms, i;
				this.makeGUIElement(arrParms, classData.wtData);
			});
			// next line
			classData.guiData.viewBox.decorator.nextLine.shift(0, 6);

			//X & Y Range Preset text & buttons
			StaticText(classData.guiData.viewBox, 120 @ 20)
			.align_(\right)
			.string_("min:max presets")
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			// preset buttons
			[
				[0, 1,],
				[-1, 1,],
				[0, 2,],
				[-2, 2,],
				[0, 0.75,],
				[-0.75, 0.75,],
				[0, 0.5,],
				[-0.5, 0.5,],
				[0, 0.333333,],
				[-0.333333, 0.333333,],
				[0, 0.25,],
				[-0.25, 0.25,],
			].do({arg item, i;
				var presetName = item[0].round(0.001).asString ++ " : " ++ item[1].round(0.001).asString;
				Button(classData.guiData.viewBox, 84 @ 20)
				.states_([[presetName, TXColor.white, TXColor.sysGuiCol2]])
				.action_({
					classData.wtData[\xMin] = item[0];
					classData.wtData[\xMax] = item[1];
					classData.wtData[\yMin] = item[0];
					classData.wtData[\yMax] = item[1];
					this.dataChanged;
					this.showWindow;
				});
			});
			// Global parameters ===========
			[
				[\newLine, 2], // height
				[\horizontalLine, 2], // height
				[\newLine, 12], // height
				[\colorRGB, [\color1H, \color1S, \color1B, ], "color1 HSB"],
				[\newLine, 4], // height
				[\colorRGB, [\color2H, \color2S, \color2B, ], "color2 HSB"],
				[\newLine, 4], // height
				[\colorRGB, [\color3H, \color3S, \color3B, ], "color3 HSB"],
			].do({arg arrParms, i;
				this.makeGUIElement(arrParms, classData.wtData);
			});
			classData.guiData.viewBox.decorator.nextLine.shift(0,4);

			holdView = Button(classData.guiData.viewBox, Rect(0, 0, 210, 24));
			holdView.states = [["Default Colours - Black/ Grey/ White", TXColor.white, TXColor.sysGuiCol2]];
			holdView.action = {
				[
					[[\color1H, \color1S, \color1B, ], [0, 0, 0]],
					[[\color2H, \color2S, \color2B, ], [0, 0, 0.5]],
					[[\color3H, \color3S, \color3B, ], [0, 0, 1]],
				].do({arg item;
					var hsvArray = item[1];
					item[0].do({arg parameter, i;
						classData.wtData[parameter] = item[1][i];
					});
				});
				this.dataChanged;
				this.showWindow;
			};

			classData.guiData.viewBox.decorator.shift(10,0);

			holdView = Button(classData.guiData.viewBox, Rect(0, 0, 210, 24));
			holdView.states = [["Open Colour Picker - drag & drop", TXColor.white, TXColor.sysGuiCol2]];
			holdView.action = {TXColor.showPicker;};

		},{
			// Layer parameters ===========
			if (classData.layerViewIndex < 5, {
				holdArrDisplayLayers = [
					[classData.wtLayerAData],
					[classData.wtLayerBData],
					[classData.wtLayerCData],
					[classData.wtLayerDData],
				][classData.layerViewIndex.asInteger - 1];
			},{
				holdArrDisplayLayers = [
					[classData.wtLayerAData],
					[classData.wtLayerBData],
					[classData.wtLayerCData],
					[classData.wtLayerDData],
					[classData.wtLayerAData, classData.wtLayerBData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData],
					[classData.wtLayerAData, classData.wtLayerBData, classData.wtLayerCData, classData.wtLayerDData],
				][classData.wtData.structureFuncIndex.asInteger];
			});
			holdArrDisplayLayers.do({arg holdLayer, i;
				var holdPresetNameView, holdLayPresetPopup, holdView, holdString, holdLayPresetSaveBtn;
				// next line
				classData.guiData.viewBox.decorator.nextLine.shift(0, 6);
				// StaticText
				holdView = StaticText(classData.guiData.viewBox, 120 @ 20);
				holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow2);
				holdView.align_(\center);
				if (classData.layerViewIndex < 5, {
					holdString = ["Layer A", "Layer B", "Layer C", "Layer D"][classData.layerViewIndex .asInteger - 1];
				}, {
					holdString = ["Layer A (part)", "Layer B (part)", "Layer C (part)", "Layer D (part)",][i];
				});
				holdView.string = holdString;

				classData.guiData.viewBox.decorator.shift(20,0);
				// Layer presets
				holdLayPresetPopup = TXPopupPlusMinus(classData.guiData.viewBox, 500 @ 20,
					"Layer presets", this.getWTLayerPresetNames,
					{arg view; holdLayer.layerPresetIndex = view.value;}, holdLayer.layerPresetIndex, labelWidth:90);
				Button(classData.guiData.viewBox, 60 @ 20)
				.states_([["Load", TXColor.white, TXColor.sysGuiCol1]])
				.action_({
					if (classData.wtLayerPresetData.keys.size > 0, {
						holdLayer.layerPresetText = this.getWTLayerPresetNames[holdLayer.layerPresetIndex];
						//holdLayer.layerPresetIndex = holdLayer.layerPresetIndex;
						this.loadWTLayerPreset(this.getWTLayerPresetNames[holdLayer.layerPresetIndex], holdLayer);
						this.dataChanged;
						this.showWindow;
					});
				});
				classData.guiData.viewBox.decorator.shift(15, 0);
				// Button(classData.guiData.viewBox, 70 @ 20)
				// .states_([["Overwrite", TXColor.white, TXColor.sysGuiCol2]])
				// .action_({
				// 	if (classData.wtLayerPresetData.keys.size > 0, {
				// 		this.saveWTLayerPreset(this.getWTLayerPresetNames[holdLayer.layerPresetIndex], holdLayer);
				// 	});
				// });
				Button(classData.guiData.viewBox, 50 @ 20)
				.states_([["Delete", TXColor.white, TXColor.sysDeleteCol]])
				.action_({
					if (classData.wtLayerPresetData.keys.size > 0, {
						this.saveWTLayerPreset(this.getWTLayerPresetNames[holdLayer.layerPresetIndex], nil);
						holdLayer.layerPresetIndex = 0;
						this.showWindow;
					});
				});
				classData.guiData.viewBox.decorator.shift(15, 0);
				// StaticText
				StaticText(classData.guiData.viewBox, 60 @ 20)
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.string_("Name").align_(\right);
				// Name field
				holdPresetNameView = TextField(classData.guiData.viewBox, 260 @ 20)
				.string_(holdLayer.layerPresetText)
				.action_({arg view;
					var holdIndex, holdString;
					holdString = this.removeInitSpaces(view.string);
					view.string = holdString;
					holdLayer.layerPresetText = holdString;
					holdIndex = this.getWTLayerPresetNames.indexOfEqual(holdString);
					if (holdIndex.notNil, {
						holdLayer.layerPresetIndex = holdIndex;
						holdLayPresetPopup.value_(holdIndex);
						holdLayPresetSaveBtn.states_([["Overwrite", TXColor.white, TXColor.sysGuiCol1]]);
					}, {
						holdLayPresetSaveBtn.states_([["Save", TXColor.white, TXColor.sysGuiCol2]]);
					});
					if (holdString == "", {
						holdLayPresetSaveBtn.visible = false;
					}, {
						holdLayPresetSaveBtn.visible = true;
					});
				});
				Button(classData.guiData.viewBox, 18 @ 20)
				.states_([["X", TXColor.white, TXColor.sysDeleteCol]])
				.action_({
					holdPresetNameView.string = "";
					holdLayer.layerPresetText =  "";
					holdLayPresetSaveBtn.visible = false;
				});
				holdLayPresetSaveBtn = Button(classData.guiData.viewBox, 86 @ 20)
				//.states_([["Save", TXColor.white, TXColor.sysGuiCol2]])
				.action_({
					var holdString;
					if (classData.wtLayerPresetData.keys.size > 0, {
						holdString = holdPresetNameView.string;
						this.saveWTLayerPreset(holdString, holdLayer);
						holdLayer.layerPresetIndex = this.getWTLayerPresetNames.indexOfEqual(holdString) ? 0;
						this.showWindow;
					});
				});
				if (this.getWTLayerPresetNames.indexOfEqual(holdLayer.layerPresetText).notNil, {
					holdLayPresetSaveBtn.states_([["Overwrite", TXColor.white, TXColor.sysGuiCol1]]);
				}, {
					holdLayPresetSaveBtn.states_([["Save", TXColor.white, TXColor.sysGuiCol2]]);
				});
				if (holdPresetNameView.string == "", {
					holdLayPresetSaveBtn.visible = false;
				}, {
					holdLayPresetSaveBtn.visible = true;
				});

				// next line
				classData.guiData.viewBox.decorator.nextLine.shift(0, 8);
				// widgets
				if (classData.layerViewIndex == 1 || {(classData.layerViewIndex == 5) && (i == 0)}, { // if not layerA
					holdArrParms = [];
				}, {
					holdArrParms = [
						[\popup, \layerJoinFuncIndex, classData.arrBinaryOpFuncs.collect({arg item; item[0];}), [0], 700],
						[\newLine, 8], // height
					];
				});
				if (classData.layerViewIndex < 5, {
					holdArrParms = holdArrParms ++ [
						[\slider, \xOffset, ControlSpec(-2, 2),
							[-2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2], 660],
						[\newLine, 4], // height
						[\slider, \yOffset, ControlSpec(-2, 2),
							[-2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2], 660],
						[\newLine, 4], // height
						[ \checkbox, \xInvert,],
						[\spacer, 20], // width
						[ \checkbox, \yInvert,],
						[\newLine, 1], // height
						[\horizontalLine, 2], // height
						[\newLine, 4], // height
						[\popup, \layerGenFuncIndex, classData.arrLayerGenFuncs.collect({arg item; item[0];}), [0], 700],
						[\newLine, 4], // height
						[\slider, \genMult, ControlSpec(-100, 100),
							[-20, -10,-3,-2,-1,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 1, 2, 3, 10, 20], 660],
						[\newLine, 4], // height
						[\popup, \genXYFuncIndex, classData.arrBinaryOpFuncs.collect({arg item; item[0];}), [0], 700 ],
						[\newLine, 4], // height
						[\slider, \genXPower, ControlSpec(0, 10), [0, 1, 2, 3, 4, 5, 10], 660],
						[\newLine, 4], // height
						[\slider, \genYPower, ControlSpec(0, 10), [0, 1, 2, 3, 4, 5, 10], 660],
						[\newLine, 4], // height
						[\slider, \genOffset, ControlSpec(-100, 100),
							[-20, -10,-3,-2,-1,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 1, 2, 3, 10, 20], 660],
						[\newLine, 1], // height
						[\horizontalLine, 2], // height
						[\newLine, 4], // height
						[\onoffbutton, \layerPost1Active, ],
						[\popup, \layerPost1FuncIndex, classData.arrLayerPostFuncs.collect({arg item; item[0];}), [], 300],
						[\spacer, 10], // width
						[\slider, \layerPost1YVal, ControlSpec(-10, 10), [-10,-2,-1,-0.5,-0.25, 0, 0.25, 0.5, 1, 2, 10], 480],
						[\newLine, 4], // height
						[\onoffbutton, \layerPost2Active, ],
						[\popup, \layerPost2FuncIndex, classData.arrLayerPostFuncs.collect({arg item; item[0];}), [], 300],
						[\spacer, 10], // width
						[\slider, \layerPost2YVal, ControlSpec(-10, 10), [-10,-2,-1,-0.5,-0.25, 0, 0.25, 0.5, 1, 2, 10], 480],
						[\newLine, 4], // height
						[\onoffbutton, \layerPost3Active, ],
						[\popup, \layerPost3FuncIndex, classData.arrLayerPostFuncs.collect({arg item; item[0];}), [], 300],
						[\spacer, 10], // width
						[\slider, \layerPost3YVal, ControlSpec(-10, 10), [-10,-2,-1,-0.5,-0.25, 0, 0.25, 0.5, 1, 2, 10], 480],
						[\newLine, 4], // height
						[\onoffbutton, \layerPost4Active, ],
						[\popup, \layerPost4FuncIndex, classData.arrLayerPostFuncs.collect({arg item; item[0];}), [], 300],
						[\spacer, 10], // width
						[\slider, \layerPost4YVal, ControlSpec(-10, 10), [-10,-2,-1,-0.5,-0.25, 0, 0.25, 0.5, 1, 2, 10], 480],
						[\newLine, 4], // height
						[\onoffbutton, \layerPost5Active, ],
						[\popup, \layerPost5FuncIndex, classData.arrLayerPostFuncs.collect({arg item; item[0];}), [], 300],
						[\spacer, 10], // width
						[\slider, \layerPost5YVal, ControlSpec(-10, 10), [-10,-2,-1,-0.5,-0.25, 0, 0.25, 0.5, 1, 2, 10], 480],
						[\newLine, 1], // height
						[\horizontalLine, 2], // height
						[\newLine, 4], // height
						[\slider, \power, ControlSpec(0, 10), [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 660],
						[\newLine, 4], // height
						[\slider, \mult, ControlSpec(-10, 10),
							[-10, -4,-3,-2,-1,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 1, 2, 3, 4, 10], 660],
						[\newLine, 4], // height
						[\slider, \multXPower, ControlSpec(0, 10), [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 660],
						[\newLine, 4], // height
						[\slider, \multYPower, ControlSpec(0, 10), [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 660],
						[\newLine, 4], // height
						[\slider, \offset, ControlSpec(-2, 2),
							[-2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2]],
					];
				}, {
					holdArrParms = holdArrParms ++ [
						[\slider, \power, ControlSpec(0, 10), [0, 0.25, 3.reciprocal, 0.5, 1, 2, 3, 4, 10]],
						[\newLine, 4], // height
						[\slider, \mult, ControlSpec(-10, 10),
							[-10, -4,-3,-2,-1,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 1, 2, 3, 4, 10]],
						[\newLine, 4], // height
						[\slider, \offset, ControlSpec(-2, 2),
							[-2,-1,-0.75,-0.5,-3.reciprocal, -0.25, 0, 0.25, 3.reciprocal, 0.5, 0.75, 1, 2]],
						[\horizontalLine, 2], // height
					];
				});
				holdArrParms.do({arg arrParms, i;
					var passLayer;
					// if parameter is valid for layer pass layer
					if (holdLayer[arrParms[1]].notNil, {
						passLayer = holdLayer;
					});
					this.makeGUIElement(arrParms, passLayer);
				});

				// next line
				classData.guiData.viewBox.decorator.nextLine.shift(0, 2);

			}); // end of holdArrDisplayLayers.do...

		}); // end of if (classData.layerViewIndex == 0, {

	} // end of showWindow

	*makeGUIElement {arg arrParms, dataEvent;
		if (arrParms[0] == \newLine, {
			classData.guiData.viewBox.decorator.nextLine.shift(0, arrParms[1] ? 4);
		}, {
			if (arrParms[0] == \label, {
				StaticText(classData.guiData.viewBox, arrParms[2] ? 120 @ 20)
				.align_(\right)
				.string_(arrParms[1])
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			}, {
				if (arrParms[0] == \spacer, {
					classData.guiData.viewBox.decorator.shift(arrParms[1] ? 10, 0);
				}, {
					if (arrParms[0] == \horizontalLine, {
						UserView(classData.guiData.viewBox,
							(classData.guiData.viewBox.bounds.width - 12) @ arrParms[1] ? 3)
						.background_(TXColor.grey(0.8));
					}, {
						if (dataEvent.notNil, {
							this.makeWidget(arrParms, dataEvent);
						});
					});
				});
			});
		});
	}

	*makeWidget {arg arrParms, dataEvent;
		var parameter = arrParms[1];
		var spec, items, actionFunc, arrPresetVals;
		var holdSlider, holdPopup, holdColor, holdWidth, holdString;
		// slider
		if (arrParms[0] == \slider, {
			spec = arrParms[2];
			holdWidth = arrParms[4] ? 660;
			holdSlider = TXSlider(classData.guiData.viewBox, holdWidth @ 20, parameter, spec,
				{arg view; dataEvent[parameter] = view.value;},
				dataEvent[parameter], labelWidth:120);
			holdSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			holdSlider.sliderView.background_(TXColor.sysGuiCol1);
			holdSlider.sliderView.mouseUpAction = {
				this.dataChanged;
			};
			holdSlider.numberView.action = {arg view;
				view.value = spec.constrain(view.value);
				dataEvent[parameter] = view.value;
				holdSlider.sliderView.value = spec.unmap(view.value);
				this.dataChanged;
			};
			// preset buttons
			arrPresetVals = arrParms[3];
			arrPresetVals.asArray.do({arg presetVal, i;
				if (presetVal.isNegative, {
					holdColor = TXColor.sysGuiCol2;
				}, {
					if (presetVal == 0, {
						holdColor = TXColor.sysDeleteCol;
					}, {
						holdColor = TXColor.sysGuiCol1;
					});
				});
				Button(classData.guiData.viewBox, 28 @ 20)
				.states_([[presetVal.asString.keep(4), TXColor.white, holdColor]])
				.action_({
					holdSlider.sliderView.value_(spec.unmap(presetVal));
					holdSlider.numberView.value_(presetVal);
					holdSlider.numberView.action.value(holdSlider.numberView);
				});
			});
		});
		if (arrParms[0] == \popup, {
			items = arrParms[2];
			holdWidth = arrParms[4] ? 700;
			holdPopup = TXPopupPlusMinus(classData.guiData.viewBox, holdWidth @ 20, parameter, items,
				{arg view;
					dataEvent[parameter] = view.value;
					this.dataChanged;
				}, dataEvent[parameter], labelWidth:120
			);
			// preset buttons
			arrPresetVals = arrParms[3];
			arrPresetVals.asArray.do({arg presetVal, i;
				if (presetVal.isNegative, {
					holdColor = TXColor.sysGuiCol2;
				}, {
					if (presetVal == 0, {
						holdColor = TXColor.sysDeleteCol;
					}, {
						holdColor = TXColor.sysGuiCol1;
					});
				});
				Button(classData.guiData.viewBox, 28 @ 20)
				.states_([[presetVal.asString.keep(4), TXColor.white, holdColor]])
				.action_({
					holdPopup.valueAction_(presetVal);
				});
			});
			// random button
			Button(classData.guiData.viewBox, 0 @ 20)
			.states_([["Rand", TXColor.white, TXColor.sysGuiCol3.blend(TXColor.grey2, 0.2)]])
			.action_({
				holdPopup.valueAction_(items.size.rand);
			});

		});
		if (arrParms[0] == \buttons, {
			actionFunc = arrParms[3];
			holdString = arrParms[4];
			StaticText(classData.guiData.viewBox, 120 @ 20)
			.align_(\right)
			.string_(holdString)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			// preset buttons
			arrParms[2].asArray.do({arg item, i;
				var presetName = item[0];
				var presetVal = item[1];
				var holdWidth = (presetName.size * 7).max(60);
				if (dataEvent[parameter] == presetVal, {
					holdColor = TXColor.yellow;
				}, {
					holdColor = TXColor.white;
				});
				Button(classData.guiData.viewBox, holdWidth @ 20)
				.states_([[presetName, holdColor, arrParms[5] ? TXColor.sysGuiCol1]])
				.action_({
					dataEvent[parameter] = presetVal;
					actionFunc.value;
				});
			});
		});
		if (arrParms[0] == \checkbox, {
			StaticText(classData.guiData.viewBox, 120 @ 20)
			.align_(\right)
			.string_(parameter)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			TXCheckBox(classData.guiData.viewBox, 200 @ 20, parameter, TXColor.sysGuiCol1.blend(Color.red, 0.15), TXColor.white,
			TXColor.white, TXColor.sysGuiCol1.blend(Color.red, 0.15))
			.value_(dataEvent[parameter])
			.action_({arg view;
				dataEvent[parameter] = view.value;
				this.dataChanged;
			});
		});
		if (arrParms[0] == \onoffbutton, {
			TXCheckBox(classData.guiData.viewBox, 30 @ 20, "", TXColor.sysGuiCol1, TXColor.grey(0.8),
				TXColor.grey(0.8), TXColor.sysGuiCol1, 7)
			.value_(dataEvent[parameter])
			.action_({arg view;
				dataEvent[parameter] = view.value;
				this.dataChanged;
			});
		});
		if (arrParms[0] == \colorRGB, {
			var holdColorBox;
			StaticText(classData.guiData.viewBox, 120 @ 20)
			.align_(\right)
			.string_(arrParms[2])
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			// holdColorBox = StaticText(classData.guiData.viewBox, 50 @ 20)
			// .background_(Color.hsv(dataEvent[arrParms[1][0]].min(0.99),
			// dataEvent[arrParms[1][1]], dataEvent[arrParms[1][2]]));

			holdColorBox = DragBoth(classData.guiData.viewBox, Rect(0, 0, 50, 50));
			holdColorBox.background_(Color.hsv(dataEvent[arrParms[1][0]].min(0.99),
				dataEvent[arrParms[1][1]], dataEvent[arrParms[1][2]]));
			holdColorBox.beginDragAction_({ arg view, x, y;
				var holdColour;
				view.dragLabel_("Colour");
				holdColour = view.background;
				// return colour
				holdColour;
			});
			holdColorBox.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Color )
			};
			holdColorBox.receiveDragHandler = {arg view;
				var holdDragColor, hsvArray;
				holdDragColor = View.currentDrag;
				view.background_(holdDragColor);
				//classData.holdColors[i] = holdDragColor;
				hsvArray = holdDragColor.asHSV;
				arrParms[1].do({arg parameter, i;
					dataEvent[parameter] = hsvArray[i];
				});
				this.dataChanged;
				this.showWindow;
			};

			arrParms[1].do({arg parameter, i;
				var holdKnob, holdNumberBox;
				holdKnob = Knob.new(classData.guiData.viewBox, 50 @ 50);
				holdKnob.value_(dataEvent[parameter]);
				holdKnob.action_({arg view;
					dataEvent[parameter] = view.value;
					holdNumberBox.value_(view.value);
					holdColorBox.background_(
						Color.hsv(dataEvent[arrParms[1][0]].min(0.99),
							dataEvent[arrParms[1][1]], dataEvent[arrParms[1][2]]));
				});
				holdKnob.mouseUpAction_({arg view;
					this.dataChanged;
				});
				holdNumberBox = NumberBox.new(classData.guiData.viewBox, Rect(0, 50, 30, 20));
				holdNumberBox.value_(dataEvent[parameter]);
				holdNumberBox.action_({arg view;
					view.value = view.value.clip(0, 1);
					dataEvent[parameter] = view.value;
					holdKnob.value_(view.value);
					this.dataChanged;
					holdColorBox.background_(
						Color.hsv(dataEvent[arrParms[1][0]].min(0.99),
							dataEvent[arrParms[1][1]], dataEvent[arrParms[1][2]]));
				});
			});
		});
	} // end of makeWidget

	*coloriseHistoryButtons {
		if (classData.guiData.histPrevBtn.notNil, {
			if (classData.historyData.histIndex > 0, {
				classData.guiData.histPrevBtn.states_([["<", TXColor.yellow, TXColor.sysGuiCol1]])
			},{
				classData.guiData.histPrevBtn.states_([["<", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
			if (classData.historyData.histIndex < (classData.historyData.historyArray.size - 1), {
				classData.guiData.histNextBtn.states_([[">", TXColor.yellow, TXColor.sysGuiCol1]])
			},{
				classData.guiData.histNextBtn.states_([[">", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
		});
	}

	*removeInitSpaces {arg argString;
		var newString = argString;
		var i = 0;
		while ( { (i < argString.size) and: {argString[i] == " "[0]} }, {
			i = i + 1;
		});
		newString = newString.keep(i - newString.size);
		^newString;
	}

	//////////////////////// rebuild & auto-build WT

	*rebuildWT {
		classData.guiData.isBuilding = true;
		classData.guiData.viewBox.background_(TXColor.orange(0.5));
		{
		this.updateTerrain;
		this.updateImage;
		this.showImageWindow;
		classData.guiData.viewBox.background_(TXColor.grey(0.7));
		classData.guiData.isBuilding = false;
		}.defer(0.05);
	}

	*checkAutoBuild {
		if (classData.autoBuild == 1, {
			this.rebuildWT;
		});
	}

	*dataChanged {
		this.addToHistory;
		this.checkAutoBuild;
	}

}

