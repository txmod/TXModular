// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// ========================================================================
/* 	NOTE - the following code generates the list below:
(
"// ==== Instance Methods ====================== ".postln;
a = TXMultiTrackTools.methods;
b = a.collect ({ arg item, i; item.name.asString;});
b.sort.do ({ arg item, i; ("// " ++ item).postln;});
"// ================================================".postln;
)
*/

// ==== Instance Methods ======================
// adjustEditNotesToSelection
// adjustSelectionToEditNotes
// adjustSelectionToEditNotesStart
// copySelForAllTracks
// deleteAllNotesInSelectionRange
// deleteSelForSelTracks
// deleteSelectedControllerVals
// deleteSelectedMarkers
// deleteSelectedNoteVals
// doWithHistory
// generateCtrlValsFromWaveTime
// generateCtrlValsFromNumCycles
// getControllerActionSpecsFromOutModuleId
// getControllerProcessStrings
// getControllerProcesses
// getHistoryData
// getLineWarpStrings
// getLineWarpVal
// getMultiTrackProcesses
// getNoteProcessStrings
// getNoteProcesses
// getOut1ControlSpecForTrack
// getSelectionCheckSnap
// hideUnSelectedTracks
// hideUnselMutedTracks
// hideUnselSelectedTracks
// humaniseEditNotesDuration
// humaniseEditNotesStart
// humaniseEditNotesStartDuration
// humaniseEditNotesVelocity
// init
// insertSpaceInSelTracks
// interpolateSelectedControlTracks
// interpolateSelectedControllerVals
// legatoiseEditNotesDuration
// makeCopyControllerData
// makeCopyNoteData
// makeLocalCopyControllerData
// makeLocalCopyNoteData
// mergeEditControllerVals
// mergeEditNotes
// mergeNewControllerVals
// mergeNewNoteOnOffs
// modifyEditNotesLength
// modifyEditNotesStartEnd
// modifyEditNotesStartEndPitch
// modifyEditNotesVel
// muteAllTracks
// muteSelectedTracks
// pasteControllerVals
// pasteCopyNoteData
// pasteInsertForSelTracks
// pasteLocalControllerVals
// pasteLocalCopyNoteData
// pasteOverForSelTracks
// pasteReplaceForSelTracks
// pasteStretchControllerVals
// pasteStretchCopyNoteData
// performAction
// performActionStep
// quantiseEditNotesDuration
// quantiseEditNotesStart
// quantiseEditNotesStartEnd
// quantiseEditNotesVelocity
// rebuildEditNotesFromSelectionIndices
// rebuildEditNotesFromSelectionStartEnd
// rebuildSelectionIndicesFromEditNotes
// redoEdit
// redoEditForSelTracks
// reindexControllerTimes
// reindexNoteTimes
// removeEditNoteOverlaps
// resetSelection
// reverseEditControllerVals
// reverseEditNotesPitch
// reverseEditNotesStart
// reverseEditNotesVelocity
// runControllerProcess
// runNoteProcess
// scaleEditNotesDuration
// scaleEditNotesStart
// scaleEditNotesStartDuration
// scaleEditNotesVelocity
// scaleEditNotesVelocityToRange
// scaleSelectionEnd
// scrambleOrderEditNotesPitch
// scrambleOrderEditNotesStart
// scrambleOrderEditNotesVelocity
// selectAllNotes
// selectAllTracks
// setControllerProcessIndex
// setMonitorForSelTracks
// setNoteProcessIndex
// setRecordForSelTracks
// setSelectionForAllTracks
// setSelectionForSelTracks
// setSelectionFromEditControllerVals
// setSelectionFromEditNotes
// setSingleEditEventTime
// setSingleEditEventVal
// setSingleEditNoteNum
// setSingleEditNoteOff
// setSingleEditNoteOn
// setSingleEditNoteVel
// shiftControllerValsFrom
// shiftNotesFrom
// shiftRemoveSelForSelTracks
// smoothSelectedControllerVals
// smoothValsForSelCtrlTracks
// snapEditControllerValsToGrid
// snapEditNotesToGrid
// snapSelectionToGrid
// storePostEditHistory
// storePreEditHistory
// swapMutedUnmutedTracks
// swapSelectedUnselectedTracks
// thickenInterpolateValsForSelCtrlTracks
// thickenSelectedControllerVals
// thickenSmoothValsForSelCtrlTracks
// thickenValsForSelCtrlTracks
// timeShiftEditNotes
// timeShiftSelectionStartEnd
// transposeEditNotes
// undoEdit
// undoEditForSelTracks
// unhideAllTracks
// unselectAllNotes
// updateControllerSelectionIndices
// writeKeyframeInSelCtrlTracks
// writeKeyframeInTrack
// ================================================

TXMultiTrackTools {		// Methods for editing tracks - called by TXMultiTrackSeq

	var parentModule; // owning TXMultiTrackSeq module
	var system;
	var preEditHistories, postEditHistories, trackHistoryIndices;

	*initClass {
		//
	}

	*new{ arg argParentModule, argSystem;
		^super.new.init(argParentModule, argSystem);
	}

	init {arg argParentModule, argSystem;
		parentModule = argParentModule;
		system = argSystem;
		preEditHistories = ();
		postEditHistories = ();
		trackHistoryIndices = ();
	}

	getHistoryData { arg track;
		var holdEvents, historyArray;
		// create edit point array
		if (track.trackType == 'Note', {
			holdEvents = track.arrNotes.deepCopy;
		});
		if (track.trackType == 'Controller', {
			this.updateControllerSelectionIndices(track);
			holdEvents = track.arrControllerVals.deepCopy;
		});
		if (track.trackType == 'Action', {
			holdEvents = track.arrActionSteps.deepCopy;
		});
		historyArray = [holdEvents, track.selectionStart.deepCopy, track.selectionEnd.deepCopy,
			track.arrTimes.deepCopy, track.lastEventEndTime.deepCopy, track.selectionIndices.deepCopy];
		^historyArray;
	}

	storePreEditHistory { arg track;
		var preEditData, holdKeepSize;
		if (preEditHistories[track.trackID].isNil, {
			preEditHistories[track.trackID] = [];
			postEditHistories[track.trackID] = [];
			trackHistoryIndices[track.trackID] = -1;
		});
		// remove any future history events
		holdKeepSize = trackHistoryIndices[track.trackID] + 1;
		if (preEditHistories[track.trackID].size > trackHistoryIndices[track.trackID], {
			preEditHistories[track.trackID] = preEditHistories[track.trackID].asArray.keep(holdKeepSize);
			postEditHistories[track.trackID] = postEditHistories[track.trackID].asArray.keep(holdKeepSize);
		});
		// store preEditHistory
		preEditData = this.getHistoryData(track);
		preEditHistories[track.trackID] = preEditHistories[track.trackID].add(preEditData);
	}

	storePostEditHistory { arg track;
		var postEditData;
		// store postEditHistory
		postEditData = this.getHistoryData(track);
		postEditHistories[track.trackID] = postEditHistories[track.trackID].add(postEditData);
		// only keep 20 edits
		preEditHistories[track.trackID] = preEditHistories[track.trackID].asArray.keep(-20);
		postEditHistories[track.trackID] = postEditHistories[track.trackID].asArray.keep(-20);
		// update current index
		trackHistoryIndices[track.trackID] = postEditHistories[track.trackID].size - 1;
	}

	doWithHistory { arg track, editFunc;
		this.storePreEditHistory(track);
		editFunc.value;
		this.storePostEditHistory(track);
	}

	undoEdit { arg track;
		var historyArray, holdKeepSize;
		if (preEditHistories[track.trackID].asArray.size > 0, {
			if (trackHistoryIndices[track.trackID] > -1, {
				// reload last history point
				historyArray = preEditHistories[track.trackID][trackHistoryIndices[track.trackID]].deepCopy;
				if (historyArray.notNil, {
					if (track.trackType == 'Note', {
						track.arrNotes = historyArray[0];
					});
					if (track.trackType == 'Controller', {
						track.arrControllerVals = historyArray[0];});
					if (track.trackType == 'Action', {track.arrActionSteps = historyArray[0];
					});
					track.selectionStart = historyArray[1];
					track.selectionEnd = historyArray[2];
					track.arrTimes = historyArray[3];
					track.lastEventEndTime = historyArray[4];
					track.selectionIndices = historyArray[5];
					if (track.trackType == 'Note', {
						track.arrNotes = historyArray[0];
						this.rebuildEditNotesFromSelectionIndices(track);
					});
					if (track.trackType == 'Controller', {
						track.arrControllerVals = historyArray[0];
						this.updateControllerSelectionIndices(track);
					});
					// update current index
					trackHistoryIndices[track.trackID] = trackHistoryIndices[track.trackID] - 1;
					// update view
					system.showView;
				});
			});
		});
	}

	redoEdit { arg track;
		var historyArray, holdKeepSize;
		if (postEditHistories[track.trackID].asArray.size > 0, {
			if (trackHistoryIndices[track.trackID] < (postEditHistories[track.trackID].size - 1), {
				// update current index
				trackHistoryIndices[track.trackID] = trackHistoryIndices[track.trackID] + 1;
				// reload next history point
				historyArray = postEditHistories[track.trackID][trackHistoryIndices[track.trackID]].deepCopy;
				if (historyArray.notNil, {
					if (track.trackType == 'Note', {
						track.arrNotes = historyArray[0];
					});
					if (track.trackType == 'Controller', {
						track.arrControllerVals = historyArray[0];
					});
					if (track.trackType == 'Action', {
						track.arrActionSteps = historyArray[0];
					});
					track.selectionStart = historyArray[1];
					track.selectionEnd = historyArray[2];
					track.arrTimes = historyArray[3];
					track.lastEventEndTime = historyArray[4];
					track.selectionIndices = historyArray[5];
					if (track.trackType == 'Note', {
						this.rebuildEditNotesFromSelectionIndices(track);
					});
					if (track.trackType == 'Controller', {
						this.updateControllerSelectionIndices(track);
					});
					// update view
					system.showView;
				});
			});
		});
	}

	snapSelectionToGrid{arg track;
		if (track.snapToGrid.asBoolean, {
			if ((track.selectionStart % track.snapBeats) > (track.snapBeats * 0.5), {
				track.selectionStart = track.snapBeats * (track.selectionStart / track.snapBeats).ceil;
			}, {
				track.selectionStart = track.snapBeats * (track.selectionStart / track.snapBeats).floor;
			});
			if ((track.selectionEnd % track.snapBeats) > (track.snapBeats * 0.5), {
				track.selectionEnd = track.snapBeats * (track.selectionEnd / track.snapBeats).ceil;
			}, {
				track.selectionEnd = track.snapBeats * (track.selectionEnd / track.snapBeats).floor;
			});
		});
	}

	getSelectionCheckSnap {arg track, time;
		var outTime = time;
		if (track == parentModule, {
			if (parentModule.getSynthArgSpec("snapToGrid").asBoolean, {
				outTime = time.round(parentModule.getSynthArgSpec("snapBeats"));
			});
		},{
			if (track.trackType == 'Controller', {
				if (track.snapToGrid.asBoolean, {
					outTime = time.round(track.snapBeats)
				});
			},{
				if (track.trackType == 'Note', {
					if (track.snapSelection.asBoolean, {
						outTime = time.round(track.snapBeats)
					});
				});
			});
		});
		^outTime;
	}

	// controller track methods //===================

	updateControllerSelectionIndices {arg track;
		var arrIndices, tempArray;
		arrIndices = [];
		tempArray = track.arrControllerVals.do({arg item, i;
			// only store indices that are in selection
			if ((item[0] >= track.selectionStart) && (item[0] < track.selectionEnd), {
				arrIndices = arrIndices.add(i);
			});
		});
		track.selectionIndices = arrIndices;
	}

	mergeEditControllerVals {arg track;
		var tempArray;
		// merge & sort events, rebuild arrTimes
		if (track.arrEditControllerVals.size > 0, {
			tempArray = track.arrControllerVals.select({arg item, i;
				// only keep items that are before selectionStart, or on or after selectionEnd
				(item[0] < track.selectionStart)
				|| (item[0] >= track.selectionEnd)
			});
			track.arrControllerVals = tempArray ++ track.arrEditControllerVals;
			track.arrControllerVals.sort({arg a, b; a[0] < b[0];});
			this.reindexControllerTimes(track);
			// reset arrEditControllerVals
			track.arrEditControllerVals = [];
		});
	}

	mergeNewControllerVals{arg track, newControllerVals, endRecTime;
		var tempArray, startRecTime;
		// merge & sort events, rebuild arrTimes
		if (newControllerVals.size > 0, {
			startRecTime = newControllerVals[0][0];
			tempArray = track.arrControllerVals.select({arg item, i;
				// only keep items that are before selectionStart, or on or after selectionEnd
				(item[0] < startRecTime) || (item[0] >= endRecTime)
			});
			track.arrControllerVals = tempArray ++ newControllerVals;
			track.arrControllerVals.sort({arg a, b; a[0] < b[0];});
			this.reindexControllerTimes(track);
			// reset arrEditControllerVals
			newControllerVals = [];
		});
	}

	reindexControllerTimes {arg track;
		track.arrTimes = track.arrControllerVals.collect({arg item, i; item[0]});
		track.lastEventEndTime = track.arrTimes.last;
		parentModule.updateEndTime(track.lastEventEndTime);
	}

	snapEditControllerValsToGrid {arg track, snapToGrid, quantizeValue;
		var modVals, oldTime, holdModulo, newTime, oldVal, newVal, outVals;
		if (snapToGrid.asBoolean || quantizeValue.asBoolean, {
			// snap to grid
			if (snapToGrid.asBoolean, {
				modVals = track.arrEditControllerVals.collect({arg item, i;
					oldTime = item[0];
					holdModulo = oldTime % track.snapBeats;
					if (holdModulo > (track.snapBeats * 0.5), {
						newTime = track.snapBeats * (oldTime / track.snapBeats).ceil;
					}, {
						newTime = track.snapBeats * (oldTime / track.snapBeats).floor;
					});
					item[0] = newTime;
				});
			},{
				modVals = track.arrEditControllerVals;
			});
			// quantize values
			if (quantizeValue.asBoolean, {
				modVals = modVals.collect({arg item, i;
					oldVal = item[1];
					newVal = oldVal.round(track.quantizeValueSteps.reciprocal);
					item[1] = newVal;
					item;
				});
			});
			// remove duplicates
			outVals = modVals.select({arg item, i;
				var keep = true;
				// only keep the first last entry at a duplicate time
				case {modVals.size == 1} {
					keep = true;
				}
				{i < (modVals.size - 1)} {
					if (modVals[i+1][0] == item[0], {
						keep = false;
					});
				}
				// {i > 0} {
				// 	if (modVals[i-1][1] == item[1], {
				// 		keep = false;
				// 	});
				// }
				;
				keep;
			});
			track.arrEditControllerVals = outVals;
		});
	}

	reverseEditControllerVals {arg track;
		var holdControllerData, newArray, startTime;
		holdControllerData = track.arrControllerVals.at(track.selectionIndices).deepCopy;
		// if not empty array
		if (holdControllerData.asArray.size > 0, {
			// if not first controller event in track
			if (track.selectionIndices[0] > 0, {
				// if first item is after selectionStart
				if (holdControllerData[0][0] != track.selectionStart, {
					// add extra value to start of selection carried over from previous event
					holdControllerData.insert(0,
						[track.selectionStart, track.arrControllerVals.at(track.selectionIndices[0] - 1)[1]]);
				});
			});
			newArray = [];
			startTime = track.selectionStart;
			holdControllerData.reverseDo({arg item, i;
				var deltaTime;
				newArray = newArray.add([startTime, item[1]]);
				deltaTime = track.selectionEnd - item[0];
				startTime = track.selectionStart + deltaTime;
			});
			track.arrEditControllerVals = newArray;
		});
	}

	setSelectionFromEditControllerVals {arg track;
		var lastEditTime, holdEnd;
		var nextItemIndex;
		if (track.arrEditControllerVals.size > 0, {
			lastEditTime = track.arrEditControllerVals.last[0];
			// if events follow this, use next event for margin
			if (track.arrControllerVals.size > 0, {
				if (track.arrControllerVals.last[0] > lastEditTime, {
					nextItemIndex = track.arrTimes.indexOf(lastEditTime);
					if (nextItemIndex.isNil, {
						nextItemIndex = track.arrTimes.indexInBetween(lastEditTime).roundUp;
					}, {
						nextItemIndex = nextItemIndex + 1;
					});
					holdEnd = min(track.arrControllerVals[nextItemIndex][0], lastEditTime + track.snapBeats);
				});
			});
			if (holdEnd.isNil, {
				holdEnd = lastEditTime + track.snapBeats;
			});
			track.selectionStart = track.arrEditControllerVals.first[0];
			track.selectionEnd = holdEnd;
		});
	}

	setControllerProcessIndex {arg track, newIndex;
		var oldProcess, newProcess, oldControlSpec1, newControlSpec1, oldControlSpec2, newControlSpec2;
		var oldName1, newName1, oldName2, newName2;
		oldProcess = this.getControllerProcesses[track.processIndex];
		track.processIndex = newIndex;
		newProcess = this.getControllerProcesses[track.processIndex];
		oldName1 = oldProcess[2][0];
		newName1 = newProcess[2][0];
		oldName2 = oldProcess[2][1];
		newName2 = newProcess[2][1];
		oldControlSpec1 = oldProcess[3][0];
		newControlSpec1 = newProcess[3][0];
		oldControlSpec2 = oldProcess[3][1];
		newControlSpec2 = newProcess[3][1];
		if (newControlSpec1.notNil && (newControlSpec1.class == ControlSpec), {
			if ((newName1 != oldName1) || (newControlSpec1 != oldControlSpec1), {
				track.processVar1 =  newControlSpec1.default;
			});
		});
		if (newControlSpec2.notNil && (newControlSpec2.class == ControlSpec), {
			if ((newName2 != oldName2) || (newControlSpec2 != oldControlSpec2), {
				track.processVar2 =  newControlSpec2.default;
			});
		});
	}

	runControllerProcess { arg track, processIndex, var1, var2;
		this.getControllerProcesses[processIndex][1].value(track, var1, var2);
	}

	deleteSelectedControllerVals {arg track;
		// remove items
		track.selectionIndices.sort.reverse.do({arg ind, i; track.arrControllerVals.removeAt(ind);});
		track.selectionIndices = [];
		this.reindexControllerTimes(track);
		parentModule.calculateEndTime;
	}

	makeCopyControllerData {arg track;
		// store copy in class as event
		parentModule.class.classData.copyControllerData = ();
		parentModule.class.classData.copyControllerData.selectionStart = track.selectionStart.copy;
		parentModule.class.classData.copyControllerData.selectionEnd = track.selectionEnd.copy;
		//parentModule.class.classData.copyControllerData.selectionIndices = track.selectionIndices;
		parentModule.class.classData.copyControllerData.selectionControllerVals = track.arrControllerVals.at(track.selectionIndices).deepCopy;
		// if not empty array
		if (parentModule.class.classData.copyControllerData.selectionControllerVals.asArray.size > 0, {
			// if not first controller event in track
			if (track.selectionIndices[0] > 0, {
				// if first item is after selectionStart
				if (parentModule.class.classData.copyControllerData.selectionControllerVals[0][0] != parentModule.class.classData.copyControllerData.selectionStart, {
					// add extra value to start of selection carried over from previous event
					parentModule.class.classData.copyControllerData.selectionControllerVals.insert(0,
						[parentModule.class.classData.copyControllerData.selectionStart, track.arrControllerVals.at(track.selectionIndices[0] - 1)[1]]);
				});
			});
		});
	}

	makeLocalCopyControllerData {arg track;
		track.localCopyControllerData = ();
		track.localCopyControllerData.selectionStart = track.selectionStart.copy;
		track.localCopyControllerData.selectionEnd = track.selectionEnd.copy;
		//track.localCopyControllerData.selectionIndices = track.selectionIndices;
		track.localCopyControllerData.selectionControllerVals = track.arrControllerVals.at(track.selectionIndices).deepCopy;
		// if not empty array
		if (track.localCopyControllerData.selectionControllerVals.asArray.size > 0, {
			// if not first controller event in track
			if (track.selectionIndices[0] > 0, {
				// if first item is after selectionStart
				if (track.localCopyControllerData.selectionControllerVals[0][0] != track.localCopyControllerData.selectionStart, {
					// add extra value to start of selection carried over from previous event
					track.localCopyControllerData.selectionControllerVals.insert(0,
						[track.localCopyControllerData.selectionStart, track.arrControllerVals.at(track.selectionIndices[0] - 1)[1]]);
				});
			});
		});
	}

	shiftControllerValsFrom{arg track, startTimeBeats, shiftBeats;
		var holdIndex, testFunction, bodyFunction;
		if ((track.arrTimes.size > 0) and: {(startTimeBeats <= track.arrTimes.last)}, {
			holdIndex = track.arrTimes.indexOf(startTimeBeats);
			if (holdIndex.isNil && (track.arrTimes.size > 1), {
				holdIndex = track.arrTimes.indexInBetween(startTimeBeats).roundUp;
			});
			if (holdIndex.notNil, {
				testFunction = {holdIndex < track.arrControllerVals.size};
				bodyFunction = {var holdTime;
					holdTime = track.arrControllerVals[holdIndex][0] + shiftBeats;
					track.arrControllerVals[holdIndex].put(0, holdTime);
					holdIndex = holdIndex + 1;
				};
				while ( testFunction, bodyFunction );
			});
			this.reindexControllerTimes(track);
		});
	}

	pasteControllerVals {arg track, startTimeBeats, endTimeBeats;
		var deltaControllerVals;
		var tempArray, copies, holdIndex, copySelectionTime;
		if (parentModule.class.classData.copyControllerData.selectionControllerVals.asArray.size > 0, {
			copySelectionTime = parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart;
			if ((parentModule.class.classData.copyControllerData.size > 0 ) && (copySelectionTime > 0), {
				deltaControllerVals = parentModule.class.classData.copyControllerData.selectionControllerVals.collect({arg item, i;
					[item[0] - parentModule.class.classData.copyControllerData.selectionStart, item[1]];
				});
				tempArray = [];
				copies = ((endTimeBeats - startTimeBeats) / copySelectionTime).ceil;
				copies.do({arg ind;
					var timeOffset = (ind * copySelectionTime) + startTimeBeats;
					deltaControllerVals.do({arg item, i;
						var holdTime = item[0] + timeOffset;
						if (holdTime < endTimeBeats, {
							tempArray = tempArray.add([holdTime, item[1]]);
						});
					});
				});
				if (tempArray.size > 0, {
					/* at the end of the paste,
					if the last item pasted time < endSelection, and if there are events after, but not on, endTimeBeats
					then insert a new event at the time of endTimeBeats with previous event's controllerVal
					*/
					if (track.arrTimes.notNil, {
						if ((tempArray.last[0] < endTimeBeats) && (track.arrTimes.last ? 0 >= endTimeBeats), {
							holdIndex = track.arrTimes.indexOf(endTimeBeats);
							if (holdIndex.isNil, {
								holdIndex = track.arrTimes.indexInBetween(endTimeBeats).floor;
								tempArray = tempArray.add([endTimeBeats, track.arrControllerVals[holdIndex][1]]);
							});
						});
					});
					track.selectionStart = startTimeBeats;
					track.selectionEnd = endTimeBeats;
					track.arrEditControllerVals = tempArray.deepCopy;
					this.mergeEditControllerVals(track);
				});
			});
		});
	}

	pasteLocalControllerVals {arg track, startTimeBeats, endTimeBeats;
		var deltaControllerVals;
		var tempArray, copies, holdIndex, copySelectionTime;
		if (track.localCopyControllerData.selectionControllerVals.asArray.size > 0, {
			copySelectionTime = track.localCopyControllerData.selectionEnd - track.localCopyControllerData.selectionStart;
			if ((track.localCopyControllerData.size > 0 ) && (copySelectionTime > 0), {
				deltaControllerVals = track.localCopyControllerData.selectionControllerVals.collect({arg item, i;
					[item[0] - track.localCopyControllerData.selectionStart, item[1]];
				});
				tempArray = [];
				copies = ((endTimeBeats - startTimeBeats) / copySelectionTime).ceil;
				copies.do({arg ind;
					var timeOffset = (ind * copySelectionTime) + startTimeBeats;
					deltaControllerVals.do({arg item, i;
						var holdTime = item[0] + timeOffset;
						if (holdTime < endTimeBeats, {
							tempArray = tempArray.add([holdTime, item[1]]);
						});
					});
				});
				if (tempArray.size > 0, {
					/* at the end of the paste,
					if the last item pasted time < endSelection, and if there are events after, but not on, endTimeBeats
					then insert a new event at the time of endTimeBeats with previous event's controllerVal
					*/
					if (track.arrTimes.notNil && (track.arrTimes.size > 0), {
						if ((tempArray.last[0] < endTimeBeats) && (track.arrTimes.last ? 0 >= endTimeBeats), {
							holdIndex = track.arrTimes.indexOf(endTimeBeats);
							if (holdIndex.isNil, {
								holdIndex = track.arrTimes.indexInBetween(endTimeBeats).floor;
								tempArray = tempArray.add([endTimeBeats, track.arrControllerVals[holdIndex][1]]);
							});
						});
					});
					track.selectionStart = startTimeBeats;
					track.selectionEnd = endTimeBeats;
					track.arrEditControllerVals = tempArray.deepCopy;
					this.mergeEditControllerVals(track);
				});
			});
		});
	}

	pasteStretchControllerVals {arg track, startTimeBeats, endTimeBeats;
		var stretchFactor, arrStretchedControlVals, orig_selectionControllerVals, orig_selectionStart, orig_selectionEnd;
		if ((parentModule.class.classData.copyControllerData.selectionControllerVals.asArray.size > 0) && (endTimeBeats > startTimeBeats), {
			// store original values
			orig_selectionControllerVals = parentModule.class.classData.copyControllerData.selectionControllerVals;
			orig_selectionEnd = parentModule.class.classData.copyControllerData.selectionEnd;
			// stretch copied notes
			stretchFactor = (endTimeBeats - startTimeBeats) / (parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart);
			arrStretchedControlVals = parentModule.class.classData.copyControllerData.selectionControllerVals.collect({arg item, i;
				var startDelta = item[0] - parentModule.class.classData.copyControllerData.selectionStart;
				var startTime = parentModule.class.classData.copyControllerData.selectionStart + (startDelta * stretchFactor);
				[startTime, item[1]];
			});
			parentModule.class.classData.copyControllerData.selectionControllerVals = arrStretchedControlVals;
			// stretch copied selectionEnd
			parentModule.class.classData.copyControllerData.selectionEnd = parentModule.class.classData.copyControllerData.selectionStart +
			((parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart) * stretchFactor);
			// paste
			this.pasteControllerVals(track, startTimeBeats, endTimeBeats);
			// restore original values (before stretch) to clipboard
			parentModule.class.classData.copyControllerData.selectionControllerVals = orig_selectionControllerVals;
			parentModule.class.classData.copyControllerData.selectionEnd = orig_selectionEnd;
		});
	}

	setSingleEditEventTime {arg track, eventTime;
		// save history with edit
		this.doWithHistory(track, {
			// change time
			track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices.keep(1)];
			track.arrEditControllerVals[0][0] = eventTime;
			this.setSelectionFromEditControllerVals(track);
			this.mergeEditControllerVals(track);
			parentModule.calculateEndTime;
		});
	}

	setSingleEditEventVal {arg track, val;
		// save history with edit
		this.doWithHistory(track, {
			// change val
			track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices.keep(1)];
			track.arrEditControllerVals[0][1] = val;
			this.mergeEditControllerVals(track);
		});
	}

	thickenSelectedControllerVals {arg track, interpolate = false;
		var tempArray, nextItem, newTime, newVal;
		if (track.selectionIndices.asArray.size > 1, {
			track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
			tempArray = [];
			track.arrEditControllerVals.do({arg item, i;
				tempArray = tempArray.add(item);
				// if not last item, add extra item in between
				if (i < (track.arrEditControllerVals.size - 1), {
					nextItem = track.arrEditControllerVals[i + 1];
					newTime = (item[0] + nextItem[0]) * 0.5;
					if (interpolate, {
						newVal = (item[1] + nextItem[1]) * 0.5;
					},{
						newVal = item[1];
					});
					tempArray = tempArray.add([newTime, newVal]);
				});
			});
			track.arrEditControllerVals = tempArray;
			this.mergeEditControllerVals(track);
			this.updateControllerSelectionIndices(track);
		});
	}

	interpolateSelectedControllerVals {arg track, upLineWarpIndex, downLineWarpIndex;
		var tempArray;
		if (track.selectionIndices.asArray.size > 1, {
			track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
			tempArray = [];
			track.arrEditControllerVals.do({arg item, i;
				var nextItem, numInsertEvents, insertStartTime;
				tempArray = tempArray.add(item);
				// if not last item, add extra items in between
				if (i < (track.arrEditControllerVals.size - 1), {
					nextItem = track.arrEditControllerVals[i + 1];
					numInsertEvents = 0;
					if (nextItem[0] > item[0], {
						numInsertEvents = ((nextItem[0] - item[0]) / track.snapBeats).floor;
						insertStartTime = (item[0]/ track.snapBeats).ceil * track.snapBeats;
						numInsertEvents.do({arg ind;
							var newTime, newVal, proportion, changeVal, lineWarpIndex;
							newTime = insertStartTime + (ind * track.snapBeats);
							proportion = (newTime - item[0]) / (nextItem[0] - item[0]);
							changeVal = nextItem[1] - item[1];
							if (changeVal >= 0, {
								lineWarpIndex = upLineWarpIndex;
							}, {
								lineWarpIndex = downLineWarpIndex;
							});
							newVal = item[1] + (changeVal * TXLineWarp.getLineWarpVal(proportion, lineWarpIndex));
							tempArray = tempArray.add([newTime, newVal]);
						});
					});
				});
			});
			track.arrEditControllerVals = tempArray;
			this.mergeEditControllerVals(track);
			this.updateControllerSelectionIndices(track);
		});
	}

	smoothSelectedControllerVals{arg track;
		var tempArray, leftRatio, leftProp, rightProp;
		if (track.selectionIndices.asArray.size > 1, {
			track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
			tempArray = [];
			track.arrEditControllerVals.do({arg item, i;
				var newVal;
				if ((i == 0) || (i == (track.arrEditControllerVals.size - 1)), {
					newVal = item[1];
				}, {
					// average values
					leftRatio = (item[0] - track.arrEditControllerVals[i-1][0])
					/ ((item[0] - track.arrEditControllerVals[i-1][0]) + (track.arrEditControllerVals[i+1][0] - item[0]));
					leftProp = track.arrEditControllerVals[i-1][1] * (1 - leftRatio) * 2;
					rightProp =  track.arrEditControllerVals[i+1][1] * leftRatio * 2;
					newVal = (item[1] + leftProp + rightProp) / 3;
				});
				tempArray = tempArray.add([item[0], newVal]);
			});
			track.arrEditControllerVals = tempArray;
			this.mergeEditControllerVals(track);
		});
	}

	generateCtrlValsFromNumCycles {arg track, genFunc;
		var startTimeQuant, endTimeQuant, totalGenTime, numEvents, newArray;
		var holdGenBeats, holdTime, holdPhaseOffset, holdRate, holdPhase, holdValue;
		startTimeQuant = track.selectionStart.roundUp(track.snapBeats);
		endTimeQuant =  (track.selectionEnd.roundUp(track.snapBeats) - (track.snapBeats)).max(startTimeQuant);
		totalGenTime = endTimeQuant - startTimeQuant;
		if (endTimeQuant > startTimeQuant, {
			numEvents = 1 + ((endTimeQuant - startTimeQuant) / track.snapBeats).asInteger;
			holdRate = track.processVar1 * 0.99999 /totalGenTime;
			holdPhaseOffset = track.processVar2;
			newArray = numEvents.collect({arg i;
				holdGenBeats = (i * track.snapBeats);
				holdTime = startTimeQuant + holdGenBeats;
				holdPhase = ((holdGenBeats * holdRate) + holdPhaseOffset) % 1.0;
				holdValue = genFunc.value(holdPhase);
				[holdTime, holdValue];
			});
			track.arrEditControllerVals = newArray;
			this.mergeEditControllerVals(track);
		});
	}

	generateCtrlValsFromWaveTime {arg track, genFunc;
		var startTimeQuant, endTimeQuant, numEvents, newArray;
		var holdGenBeats, holdTime, holdPhaseOffset, holdRate, holdPhase, holdValue;
		startTimeQuant = track.selectionStart.roundUp(track.snapBeats);
		endTimeQuant =  (track.selectionEnd.roundUp(track.snapBeats) - (track.snapBeats)).max(startTimeQuant);
		if (endTimeQuant > startTimeQuant, {
			numEvents = 1 + ((endTimeQuant - startTimeQuant) / track.snapBeats).asInteger;
			holdRate = track.processVar1.reciprocal;
			holdPhaseOffset = track.processVar2;
			newArray = numEvents.collect({arg i;
				holdGenBeats = (i * track.snapBeats);
				holdTime = startTimeQuant + holdGenBeats;
				holdPhase = ((holdGenBeats * holdRate) + holdPhaseOffset) % 1.0;
				holdValue = genFunc.value(holdPhase);
				[holdTime, holdValue];
			});
			track.arrEditControllerVals = newArray;
			this.mergeEditControllerVals(track);
		});
	}

	getControllerActionSpecsFromOutModuleId {arg holdTrack, holdModuleID;
		var holdArrActionSpecs;
		var tempModule = system.getModuleFromID(holdModuleID);
		if (tempModule == 0, {tempModule = system});
		// OLD:
		// holdArrActionSpecs = tempModule.arrActionSpecs.select({arg action, i;
		// 	(action.actionType == \valueAction) && (action.guiObjectType == \number)
		// 	&& (action.arrControlSpecFuncs.size == 1);
		// });
		holdArrActionSpecs = tempModule.arrActionSpecs.select({arg action, i;
			(action.actionType == \valueAction)
			&& (((action.guiObjectType == \number) && (action.arrControlSpecFuncs.size == 1))
				|| (action.guiObjectType == \checkbox) || (action.guiObjectType == \popup));
		});
		^holdArrActionSpecs;
	}

	getControllerProcesses {
		// FORMAT: "Name", {arg track; "ACTION";}, ["arrNames for variables"], ["arrControlSpecs for variables"]],
		^[
			["copy selected events", {arg track;
				this.makeCopyControllerData(track);
			}, [], []],
			["cut & leave space", {arg track;
				this.makeCopyControllerData(track);
				this.deleteSelectedControllerVals(track);
			}, [], []],
			["cut & remove space", {arg track;
				this.makeCopyControllerData(track);
				this.deleteSelectedControllerVals(track);
				this.shiftControllerValsFrom(track, track.selectionEnd, track.selectionStart - track.selectionEnd);
			}, [], []],
			// ["generate perlin noise", {arg track;
			// 	this.generateCtrlValsFromWaveTime(track, {arg inPhase; 1.0.rand;});
			// }, ["Rate"], [ControlSpec(0.1,1000, 'exp', default: 1)]],
			["generate random values", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; 1.0.rand;});
			}, [], []],
			["generate ramp down wave (using wave time)", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; 1 - inPhase;});
			}, ["Wave time beats", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate ramp down wave (using no. cycles)", {arg track;
				this.generateCtrlValsFromNumCycles(track, {arg inPhase; 1 - inPhase;});
			}, ["No. cycles", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate ramp up wave (using wave time)", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; inPhase;});
			}, ["Wave time beats", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate ramp up wave (using no. cycles)", {arg track;
				this.generateCtrlValsFromNumCycles(track, {arg inPhase; inPhase;});
			}, ["No. cycles", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate sine wave (using wave time)", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; sin(inPhase * 360.degrad).linlin(-1,1,0,1);});
			}, ["Wave time beats", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate sine wave (using no. cycles)", {arg track;
				this.generateCtrlValsFromNumCycles(track, {arg inPhase; sin(inPhase * 360.degrad).linlin(-1,1,0,1);});
			}, ["No. cycles", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate square wave (using wave time)", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; 1 - ((inPhase * 2.0) % 1.0;).round;});
			}, ["Wave time beats", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate square wave (using no. cycles)", {arg track;
				this.generateCtrlValsFromNumCycles(track, {arg inPhase; 1 - ((inPhase * 2.0) % 1.0;).round;});
			}, ["No. cycles", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate triangular wave (using wave time)", {arg track;
				this.generateCtrlValsFromWaveTime(track, {arg inPhase; ((0.25 + inPhase) * 2.0).fold(0,1);});
			}, ["Wave time beats", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["generate triangular wave (using no. cycles)", {arg track;
				this.generateCtrlValsFromNumCycles(track, {arg inPhase; ((0.25 + inPhase) * 2.0).fold(0,1);});
			}, ["No. cycles", "Start Phase"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["insert current value of Output 1 at current time", {arg track;
				this.writeKeyframeInTrack(track, true);
				system.showView;
			}, [], []],
			["insert space", {arg track;
				this.shiftControllerValsFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
			}, [], []],
			["interpolate, add new points on snap grid", {arg track;
				this.interpolateSelectedControllerVals(track, track.processVar1.asInteger, track.processVar1.asInteger);
				this.reindexControllerTimes(track);
				this.updateControllerSelectionIndices(track);
			}, ["Curve"], [TXLineWarp.getLineWarpStrings]],
			["interpolate +/-, add new points on snap grid", {arg track;
				this.interpolateSelectedControllerVals(track, track.processVar1.asInteger, track.processVar2.asInteger);
				this.reindexControllerTimes(track);
				this.updateControllerSelectionIndices(track);
			}, ["Curve +", "Curve -"], [TXLineWarp.getLineWarpStrings, TXLineWarp.getLineWarpStrings]],
			["invert values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], 1 - item[1]];
				});
				this.mergeEditControllerVals(track);
			}, [], []],
			["maximise values", {arg track;
				track.arrEditControllerVals = [[track.selectionStart, 1]];
				this.mergeEditControllerVals(track);
			}, [], []],
			["minimise values", {arg track;
				track.arrEditControllerVals = [[track.selectionStart, 0]];
				this.mergeEditControllerVals(track);
			}, [], []],
			["normalise values to range", {arg track;
				var normVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i; item[1]});
				normVals = normVals.normalize;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], normVals[i].linlin(0, 1, track.processVar1, track.processVar2)];
				});
				this.mergeEditControllerVals(track);
			}, ["Min", "Max"], [ControlSpec(0, 1, default: 0), ControlSpec(0, 1, default: 1)]],
			["offset values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], (item[1] + track.processVar1).max(0).min(1)];
				});
				this.mergeEditControllerVals(track);
			}, ["Offset by"], [ControlSpec(-1, 1, default: 0)]],
			["overwrite with current value of Output 1", {arg track;
				this.writeKeyframeInTrack(track, false);
				system.showView;
			}, [], []],
			["paste over multiple - fill selected time", {arg track;
				this.pasteControllerVals (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste over multiple at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyControllerData.selectionEnd.notNil, {
					// use repeats to multiply pasteTime
					pasteTime = track.processVar1 * (parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart);
					this.pasteControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, ["Repeats"], [ControlSpec(0,100, default: 4)]],
			["paste over once - stretch to selected time", {arg track;
				this.pasteStretchControllerVals (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste over once at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyControllerData.selectionEnd.notNil, {
					pasteTime = parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart;
					this.pasteControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, [], []],
			["paste insert multiple - fill selected time", {arg track;
				// insert space before paste
				this.shiftControllerValsFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
				this.pasteControllerVals (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste insert multiple at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyControllerData.selectionEnd.notNil, {
					// use repeats to multiply pasteTime
					pasteTime = track.processVar1 * (parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart);
					// insert space  before paste
					this.shiftControllerValsFrom(track, track.selectionStart, pasteTime);
					this.pasteControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, ["Repeats"], [ControlSpec(0,100, default: 4)]],
			["paste insert once - stretch to  selected time", {arg track;
				// insert space  before paste
				this.shiftControllerValsFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
				this.pasteStretchControllerVals (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste insert once at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyControllerData.selectionEnd.notNil, {
					pasteTime = parentModule.class.classData.copyControllerData.selectionEnd - parentModule.class.classData.copyControllerData.selectionStart;
					// insert space before paste
					this.shiftControllerValsFrom(track, track.selectionStart, pasteTime);
					this.pasteControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, [], []],
			["quantise values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
				this.snapEditControllerValsToGrid(track, false, true);
				this.mergeEditControllerVals(track);
			}, [], []],
			["randomise values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], (1.0.rand * track.processVar1) + (item[1] * (1 - track.processVar1))];
				});
				this.mergeEditControllerVals(track);
			}, ["Randomisation"], [ControlSpec(0, 1, default: 0.1)]],
			["reverse in time", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[track.selectionStart + (track.selectionEnd - item[0]), item[1]];
				});
				track.arrEditControllerVals = track.arrEditControllerVals.reverse;
				this.reverseEditControllerVals(track);
				this.mergeEditControllerVals(track);
				this.updateControllerSelectionIndices(track);
			}, [], []],
			["scale values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], (item[1] * track.processVar1).max(0).min(1)];
				});
				this.mergeEditControllerVals(track);
			}, ["Multiply by"], [ControlSpec(0.01, 100, 'exp', default: 1)]],
			["scale values to range", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices].collect({arg item, i;
					[item[0], item[1].linlin(0, 1, track.processVar1, track.processVar2)];
				});
				this.mergeEditControllerVals(track);
			}, ["Min", "Max"], [ControlSpec(0, 1, default: 0), ControlSpec(0, 1, default: 1)]],
			["smooth", {arg track;
				track.processVar1.asInteger.do({this.smoothSelectedControllerVals(track);});
			}, ["Smoothings"], [ControlSpec(1, 10, step: 1, default: 1)]],
			["snap to grid", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
				this.snapEditControllerValsToGrid(track, true, false);
				this.mergeEditControllerVals(track);
			}, [], []],
			["snap to grid & quantise values", {arg track;
				track.arrEditControllerVals = track.arrControllerVals[track.selectionIndices];
				this.snapEditControllerValsToGrid(track, true, true);
				this.mergeEditControllerVals(track);
			}, [], []],
			["thicken, add new points", {arg track;
				this.thickenSelectedControllerVals(track, interpolate: false);
				this.reindexControllerTimes(track);
				this.updateControllerSelectionIndices(track);
			}, [], []],
			["thicken & interpolate, add new points", {arg track;
				this.thickenSelectedControllerVals(track, interpolate: true);
				this.reindexControllerTimes(track);
				this.updateControllerSelectionIndices(track);
			}, [], []],
			["thicken & smooth, add new points", {arg track;
				this.thickenSelectedControllerVals(track, interpolate: true);
				this.reindexControllerTimes(track);
				this.updateControllerSelectionIndices(track);
				this.smoothSelectedControllerVals(track);
			}, [], []],
		];
	}

	getControllerProcessStrings {
		^this.getControllerProcesses.collect({arg item; item[0]});
	}

	//// Note track methods ////////////////////////////////


	mergeNewNoteOnOffs {arg track, newNoteOnOffs;
		if (newNoteOnOffs.size > 0, {
			track.arrNotes = track.arrNotes ++ newNoteOnOffs;
			track.arrNotes.sort({arg a, b; a[0] < b[0];});
			this.reindexNoteTimes(track);
		});
	}

	mergeEditNotes {arg track;
		if (track.arrEditNotes.size > 0, {
			track.selectionIndices.do({arg index, i;
				track.arrNotes[index] = track.arrEditNotes[i];
			});
			track.arrNotes.sort({arg a, b; a[0] < b[0];});
			this.reindexNoteTimes(track);
			track.arrEditNotes.sort({arg a, b; a[0] < b[0];});
			this.rebuildSelectionIndicesFromEditNotes(track);
		});
	}

	reindexNoteTimes {arg track;
		track.lastEventEndTime = 0;
		track.arrTimes = track.arrNotes.collect({arg noteEvent, i;
			// update end time while iterating
			track.lastEventEndTime = max(track.lastEventEndTime, noteEvent[4]);
			noteEvent[0];
		});
		parentModule.updateEndTime(track.lastEventEndTime);
	}

	rebuildEditNotesFromSelectionStartEnd {arg track;
		var holdIndex, newArray, holdIndexInbetween;
		newArray = [];
		holdIndex = track.arrTimes.indexOf(track.selectionStart);
		holdIndexInbetween = track.arrTimes.indexInBetween(track.selectionStart);
		if (holdIndex.isNil && (holdIndexInbetween.notNil), {
			holdIndex = holdIndexInbetween.floor;
		});
		if (holdIndex.notNil, {
			while ({holdIndex < track.arrTimes.size}, {
				// if valid time
				if ((track.arrNotes[holdIndex][0] >= track.selectionStart)
					&& (track.arrNotes[holdIndex][0] < track.selectionEnd), {
						newArray = newArray.add(track.arrNotes[holdIndex]);
				});
				if (track.arrNotes[holdIndex][0] > track.selectionEnd, {
					// else force end of while
					holdIndex = track.arrTimes.size;
				},{
					holdIndex = holdIndex + 1;
				});
			});
			track.arrEditNotes = newArray;
		});
	}

	rebuildSelectionIndicesFromEditNotes {arg track;
		track.selectionIndices = [];
		track.arrEditNotes.do({arg noteData, i;
			var holdIndex, foundIndex;
			holdIndex = track.arrTimes.indexOf(noteData[0]);
			if (holdIndex.notNil, {
				while ({foundIndex.isNil && (holdIndex < track.arrTimes.size)}, {
					// if valid time
					if (track.arrNotes[holdIndex][0] == noteData[0], {
						if (track.arrNotes[holdIndex] == noteData, {
							foundIndex = holdIndex;
							track.selectionIndices = track.selectionIndices.add(foundIndex);
						});
						holdIndex = holdIndex + 1;
					},{
						// else force end of while
						holdIndex = track.arrTimes.size;
					});
				});
			});
		});
	}

	rebuildEditNotesFromSelectionIndices {arg track;
		track.arrEditNotes = track.selectionIndices.collect({arg index, i;
			track.arrNotes[index];
		});
	}


	selectAllNotes {arg track;
		track.selectionIndices = track.arrNotes.collect({arg item, i; i;});
		this.rebuildEditNotesFromSelectionIndices(track);
	}

	unselectAllNotes {arg track;
		track.selectionIndices = [];
		track.arrEditNotes = [];
		track.selectionStart = 0;
		track.selectionEnd = 0;
	}

	modifyEditNotesVel {arg track, scaleVel;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		track.arrEditNotes.do({arg noteEventData, i;
			noteEventData[2] = (noteEventData[2] * scaleVel).clip(0.01, 127);
		});
	}

	modifyEditNotesStartEnd {arg track, deltaTime;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		// can't move before time 0
		deltaTime = max(deltaTime, track.arrEditNotes[0][0].neg);
		track.arrEditNotes.do({arg noteEventData, i;
			noteEventData[0] = noteEventData[0] + deltaTime;
			noteEventData[4] = noteEventData[0] + noteEventData[3];
		});
	}

	modifyEditNotesStartEndPitch {arg track, deltaTime, deltaPitch;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		// can't move before time 0
		deltaTime = max(deltaTime, track.arrEditNotes[0][0].neg);
		track.arrEditNotes.do({arg noteEventData, i;
			noteEventData[0] = noteEventData[0] + deltaTime;
			noteEventData[4] = noteEventData[0] + noteEventData[3];
			noteEventData[1] = (noteEventData[1] + deltaPitch).clip(0, 120);
		});
	}

	modifyEditNotesLength {arg track, scaleLength;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		track.arrEditNotes.do({arg noteEventData, i;
			noteEventData[3] = noteEventData[3] * scaleLength;
			noteEventData[4] = noteEventData[0] + noteEventData[3];
		});
	}

	snapEditNotesToGrid {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		track.arrEditNotes.do({arg noteEventData, i;
			if (track.snapNoteStartToGrid.asBoolean , {
				noteEventData[0] = noteEventData[0].round(track.snapBeats);
				if (track.snapNoteEndToGrid.asBoolean , {
					noteEventData[4] = noteEventData[4].round(track.snapBeats);
					// minimum note length is 0.01
					noteEventData[3] = (noteEventData[4] - noteEventData[0]).max(track.snapBeats);
				});
				noteEventData[4] = noteEventData[0] + noteEventData[3];
			}, {
				if (track.snapNoteEndToGrid.asBoolean , {
					noteEventData[4] = noteEventData[4].round(track.snapBeats);
					// minimum note length is 0.01
					noteEventData[3] = (noteEventData[4] - noteEventData[0]).max(0.05);
					noteEventData[4] = noteEventData[0] + noteEventData[3];
				});
			});
		});
	}

	resetSelection{arg track;
		track.selectionStart = 0;
		track.selectionEnd = 0;
	}

	setSelectionFromEditNotes {arg track;
		var holdEnd = 0;
		if (track.arrEditNotes.size > 0, {
			track.arrEditNotes.do({arg item, i;
				holdEnd = max(item[4], holdEnd);
			});
			track.selectionStart = track.arrEditNotes.first[0];
			track.selectionEnd = holdEnd;
		},{
			track.selectionStart = 0;
			track.selectionEnd = 0;
		});
	}

	adjustSelectionToEditNotes {arg track;
		var holdEnd;
		if (track.arrEditNotes.size > 0, {
			holdEnd = 0;
			track.arrEditNotes.do({arg item, i;
				holdEnd = max(item[4], holdEnd);
			});
			track.selectionStart = min(track.selectionStart, track.arrEditNotes.first[0]);
			track.selectionEnd = max(track.selectionEnd, holdEnd);
		},{
			track.selectionStart = 0;
			track.selectionEnd = 0;
		});
	}

	adjustSelectionToEditNotesStart {arg track;
		if (track.arrEditNotes.size > 0, {
			track.selectionStart = track.selectionStart.min(track.arrEditNotes[0][0]);
			track.selectionEnd = track.selectionEnd.max(track.arrEditNotes.last[0] + 0.01);
		});
	}

	adjustEditNotesToSelection {arg track;
		var tempArrayNotes, tempArrayIndices;
		if (track.arrEditNotes.size > 0, {
			tempArrayNotes = [];
			tempArrayIndices = [];
			track.arrEditNotes.do({arg item, i;
				if ((item[0] >= track.selectionStart) && (item[0] < track.selectionEnd), {
					tempArrayNotes = tempArrayNotes.add(item);
					tempArrayIndices = tempArrayIndices.add(track.selectionIndices[i]);
				});
			});
			track.arrEditNotes = tempArrayNotes;
			track.selectionIndices = tempArrayIndices;
		});
	}

	setSingleEditNoteOn {arg track, noteOnTimeBeats;
		// save history with edit
		this.doWithHistory(track, {
			// change note start & end
			track.arrEditNotes[0][0] = noteOnTimeBeats;
			track.arrEditNotes[0][4] = noteOnTimeBeats + track.arrEditNotes[0][3];
			this.mergeEditNotes(track);
			this.setSelectionFromEditNotes(track);
			parentModule.calculateEndTime;
		});
	}
	setSingleEditNoteOff {arg track, noteOffTimeBeats;
		noteOffTimeBeats = noteOffTimeBeats.max(track.arrEditNotes[0][0] + 0.01);
		// save history with edit
		this.doWithHistory(track, {
			// change note length & end
			track.arrEditNotes[0][3] = noteOffTimeBeats - track.arrEditNotes[0][0];
			track.arrEditNotes[0][4] = noteOffTimeBeats;
			this.mergeEditNotes(track);
			this.setSelectionFromEditNotes(track);
			parentModule.calculateEndTime;
		});
	}
	setSingleEditNoteVel {arg track, noteVel;
		// save history with edit
		this.doWithHistory(track, {
			// change note vel
			track.arrEditNotes[0][2] = noteVel;
			this.mergeEditNotes(track);
			this.setSelectionFromEditNotes(track);
			parentModule.calculateEndTime;
		});
	}
	setSingleEditNoteNum {arg track, noteNum;
		// save history with edit
		this.doWithHistory(track, {
			// change note vel
			track.arrEditNotes[0][1] = noteNum;
			this.mergeEditNotes(track);
			this.setSelectionFromEditNotes(track);
			parentModule.calculateEndTime;
		});
	}

	// NOTE PROCESSES: =============

	setNoteProcessIndex {arg track, newIndex;
		var oldProcess, newProcess, oldControlSpec1, newControlSpec1, oldControlSpec2, newControlSpec2;
		var oldName1, newName1, oldName2, newName2;
		oldProcess = this.getNoteProcesses[track.processIndex];
		track.processIndex = newIndex;
		newProcess = this.getNoteProcesses[track.processIndex];
		oldName1 = oldProcess[2][0];
		newName1 = newProcess[2][0];
		oldName2 = oldProcess[2][1];
		newName2 = newProcess[2][1];
		oldControlSpec1 = oldProcess[3][0];
		newControlSpec1 = newProcess[3][0];
		oldControlSpec2 = oldProcess[3][1];
		newControlSpec2 = newProcess[3][1];
		if (newControlSpec1.notNil, {
			if ((newName1 != oldName1) || (newControlSpec1 != oldControlSpec1), {
				track.processVar1 =  newControlSpec1.default;
			});
		});
		if (newControlSpec2.notNil, {
			if ((newName2 != oldName2) || (newControlSpec2 != oldControlSpec2), {
				track.processVar2 =  newControlSpec2.default;
			});
		});
	}

	runNoteProcess { arg track, processIndex, var1, var2;
		this.getNoteProcesses[processIndex][1].value(track, var1, var2);
	}

	getNoteProcessStrings {
		^this.getNoteProcesses.collect({arg item; item[0];});
	}

	getNoteProcesses {
		// FORMAT: "Name", {arg track; "ACTION";}, ["arrNames for variables"], ["arrControlSpecs for variables"]],
		^[
			// ["Copy Selected Notes", {arg track;
			// 	this.makeCopyNoteData(track);
			// }, [], []],
			["cut selected notes", {arg track;
				this.makeCopyNoteData(track);
				this.deleteSelectedNoteVals(track);
			}, [], []],
			["cut all notes in selected time & close gap", {arg track;
				this.makeCopyNoteData(track);
				this.deleteSelectedNoteVals(track);
				this.deleteAllNotesInSelectionRange(track);
				this.shiftNotesFrom(track, track.selectionEnd, track.selectionStart - track.selectionEnd);
			}, [], []],
			["fade velocity, curve: linear", {arg track;
				this.fadeEditNotesVelocity(track, "linear");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: exp", {arg track;
				this.fadeEditNotesVelocity(track, "exp");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: 2", {arg track;
				this.fadeEditNotesVelocity(track, "2");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: 1", {arg track;
				this.fadeEditNotesVelocity(track, "1");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: -1", {arg track;
				this.fadeEditNotesVelocity(track, "-1");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: -2", {arg track;
				this.fadeEditNotesVelocity(track, "-2");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["fade velocity, curve: -exp", {arg track;
				this.fadeEditNotesVelocity(track, "-exp");
				this.mergeEditNotes(track);
			}, ["Start Vel", "End Vel"], [ControlSpec(1, 100, \amp, default: 10), ControlSpec(1, 100, \amp, default: 100)]],
			["humanise duration only - early or late", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesDuration(track, positiveOnly: false);
					this.mergeEditNotes(track);
				});
			}, ["Dur. beats"], [ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise duration only - late only", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesDuration(track, positiveOnly: true);
					this.mergeEditNotes(track);
				});
			}, ["Dur. beats"], [ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise start & duration - early or late", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesStartDuration(track, positiveOnly: false);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Start beats", "Dur. beats"],
			[ControlSpec(0.01, 100, \exp, default: 0.1), ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise start & duration - late only", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesStartDuration(track, positiveOnly: true);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Start beats", "Dur. beats"],
			[ControlSpec(0.01, 100, \exp, default: 0.1), ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise start only - early or late", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesStart(track, positiveOnly: false);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Start beats"], [ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise start only - late only", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.humaniseEditNotesStart(track, positiveOnly: true);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Start beats"], [ControlSpec(0.01, 100, \exp, default: 0.1)]],
			["humanise velocity", {arg track;
				this.humaniseEditNotesVelocity(track);
				this.mergeEditNotes(track);
			}, ["Velocity"], [ControlSpec(1, 100, \amp, default: 5)]],
			["insert space at selected time", {arg track;
				this.shiftNotesFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
			}, [], []],
			["legato - fill gaps", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.legatoiseEditNotesDuration(track);
					this.mergeEditNotes(track);
				});
			}, ["Min Note Length", "Max Length"], [ControlSpec(0.01, 1, default: 0.25), ControlSpec(0.01, 100, default: 16)]],
			["paste add multiple - fill selected time", {arg track;
				this.pasteCopyNoteData (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste add multiple at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyNoteData.selectionEnd.notNil, {
					// use repeats to multiply pasteTime
					pasteTime = track.processVar1 * (parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart);
					this.pasteCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, ["Repeats"], [ControlSpec(0,100, default: 4)]],
			["paste add once - stretch start & duration to selected time", {arg track;
				this.pasteStretchCopyNoteData (track, track.selectionStart, track.selectionEnd, true);
			}, [], []],
			["paste add once - stretch start only to selected time", {arg track;
				this.pasteStretchCopyNoteData (track, track.selectionStart, track.selectionEnd, false);
			}, [], []],
			["paste add once at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyNoteData.selectionEnd.notNil, {
					pasteTime = parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart;
					this.pasteCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, [], []],
			["paste insert multiple - fill selected time", {arg track;
				// insert space & reindex before paste
				this.shiftNotesFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
				this.pasteCopyNoteData (track, track.selectionStart, track.selectionEnd);
			}, [], []],
			["paste insert multiple at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyNoteData.selectionEnd.notNil, {
					// use repeats to multiply pasteTime
					pasteTime = track.processVar1 * (parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart);
					// insert space & reindex before paste
					this.shiftNotesFrom(track, track.selectionStart, pasteTime);
					this.pasteCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, ["Repeats"], [ControlSpec(0,100, default: 4)]],
			["paste insert once - stretch start & duration to selected time", {arg track;
				// insert space & reindex before paste
				this.shiftNotesFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
				this.pasteStretchCopyNoteData (track, track.selectionStart, track.selectionEnd, true);
			}, [], []],
			["paste insert once - stretch start only to selected time", {arg track;
				// insert space & reindex before paste
				this.shiftNotesFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
				this.pasteStretchCopyNoteData (track, track.selectionStart, track.selectionEnd, false);
			}, [], []],
			["paste insert once at selection start", {arg track;
				var pasteTime;
				if (parentModule.class.classData.copyNoteData.selectionEnd.notNil, {
					pasteTime = parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart;
					// insert space & reindex before paste
					this.shiftNotesFrom(track, track.selectionStart, pasteTime);
					this.pasteCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
				});
			}, [], []],
			["quantise note duration to snap time", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.quantiseEditNotesDuration(track);
					this.mergeEditNotes(track);
				});
			}, ["Strength"], [ControlSpec(0, 1, default: 1)]],
			["quantise note start to snap grid + swing", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.quantiseEditNotesStart(track);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Strength", "Swing"], [ControlSpec(0, 1, default: 1), ControlSpec(-1, 1, default: 0)]],
			["quantise note start & end to snap grid + swing", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.quantiseEditNotesStartEnd(track);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotesStart(track);
				});
			}, ["Strength", "Swing"], [ControlSpec(0, 1, default: 1), ControlSpec(-1, 1, default: 0)]],
			["quantise velocity", {arg track;
				this.quantiseEditNotesVelocity(track);
				this.mergeEditNotes(track);
			}, ["No. Levels", "Strength"], [ControlSpec(1, 20, step:1, default: 0), ControlSpec(0, 1, default: 1)]],
			["random note deletion", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.deleteSelectedNoteVals(track, track.processVar1);
				});
			}, ["Probability"], [ControlSpec(0.01, 0.99, default: 0.5)]],
			["remove note overlaps", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.removeEditNoteOverlaps(track);
					this.mergeEditNotes(track);
				});
			}, ["Min Note Length"], [ControlSpec(0.01, 1, default: 0.25)]],
			["reverse time order", {arg track;
				this.reverseEditNotesStart(track);
				this.mergeEditNotes(track);
			}, [], []],
			["reverse pitch order", {arg track;
				this.reverseEditNotesPitch(track);
				this.mergeEditNotes(track);
			}, [], []],
			["reverse velocity order", {arg track;
				this.reverseEditNotesVelocity(track);
				this.mergeEditNotes(track);
			}, [], []],
			["scale start & duration", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.scaleEditNotesStartDuration(track);
					this.mergeEditNotes(track);
					this.scaleSelectionEnd(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale start only", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.scaleEditNotesStart(track);
					this.mergeEditNotes(track);
					this.scaleSelectionEnd(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale duration only", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.scaleEditNotesDuration(track);
					this.mergeEditNotes(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale start & duration for all notes in selected time", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.rebuildEditNotesFromSelectionStartEnd(track);
					this.rebuildSelectionIndicesFromEditNotes(track);
					this.scaleEditNotesStartDuration(track);
					this.mergeEditNotes(track);
					this.scaleSelectionEnd(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale start & dur. for all notes in selec. time, shift following", {arg track;
				var scaledEnd = track.selectionStart + (track.selectionEnd - track.selectionStart) * track.processVar1;
				if (track.arrEditNotes.asArray.size > 0, {
					this.shiftNotesFrom(track, track.selectionEnd, scaledEnd - track.selectionEnd);
					this.rebuildEditNotesFromSelectionStartEnd(track);
					this.rebuildSelectionIndicesFromEditNotes(track);
					this.scaleEditNotesStartDuration(track);
					this.mergeEditNotes(track);
					this.scaleSelectionEnd(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale start only for all notes in selected time", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.rebuildEditNotesFromSelectionStartEnd(track);
					this.rebuildSelectionIndicesFromEditNotes(track);
					this.scaleEditNotesStart(track);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotes(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale start only for all notes in select. time, shift following", {arg track;
				var scaledEnd = track.selectionStart + (track.selectionEnd - track.selectionStart) * track.processVar1;
				if (track.arrEditNotes.asArray.size > 0, {
					this.shiftNotesFrom(track, track.selectionEnd, scaledEnd - track.selectionEnd);
					this.rebuildEditNotesFromSelectionStartEnd(track);
					this.rebuildSelectionIndicesFromEditNotes(track);
					this.scaleEditNotesStart(track);
					this.mergeEditNotes(track);
					this.adjustSelectionToEditNotes(track);
				});
			}, ["Scale"], [ControlSpec(0.01, 100, \exp, default: 1)]],
			["scale velocity", {arg track;
				this.scaleEditNotesVelocity(track);
				this.mergeEditNotes(track);
			}, ["Scale"], [ControlSpec(0.1, 10, \exp, default: 1)]],
			["scale velocity to range", {arg track;
				this.scaleEditNotesVelocityToRange(track);
				this.mergeEditNotes(track);
			}, ["Min", "Max"], [ControlSpec(0, 100, default: 0), ControlSpec(0, 100, default: 100)]],
			["scramble pitch order", {arg track;
				this.scrambleOrderEditNotesPitch(track);
				this.mergeEditNotes(track);
			}, [], []],
			["scramble start time order", {arg track;
				this.scrambleOrderEditNotesStart(track);
				this.mergeEditNotes(track);
			}, [], []],
			["scramble velocity order", {arg track;
				this.scrambleOrderEditNotesVelocity(track);
				this.mergeEditNotes(track);
			}, [], []],
			["time shift", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.timeShiftEditNotes(track);
					this.mergeEditNotes(track);
					this.timeShiftSelectionStartEnd(track);
				});
			}, ["Bars", "Beats"], [ControlSpec(-100, 100, default: 0), ControlSpec(-100, 100, default: 0)]],
			["transpose", {arg track;
				if (track.arrEditNotes.asArray.size > 0, {
					this.transposeEditNotes(track);
					this.mergeEditNotes(track);
				});
			}, ["Octaves", "Semitones"], [ControlSpec(-8, 8, step:1, default: 0), ControlSpec(-127, 127, step:1, default: 0)]],

			/* TODO:
			// Groove Quantise Start & Duration & Velocity - based on presets on separate Groove Quantise Presets tab
			["groove quantise start, duration & velocity", {arg track;
			//this.xxyxxyxx(track);
			}, [], []],
			// Generate Notes - based on scales, chords, modes, with rhythms/strum/etc,
			//     note order - random/up/down/up-down,
			["generate notes", {arg track;
			//this.xxyxxyxx(track);
			}, [], []],
			// fit notes to scale/ mode/ chord
			["fit notes to scale/ mode/ chord", {arg track;
			//this.xxyxxyxx(track);
			}, [], []],
			*/

			// TEMPLATE:==========
			// ["XXXX", {arg track;
			// 	//this.xxyxxyxx(track);
			// }, [], []],
		];
	}

	//// Action track methods ////////////////////////////////

	performActionStep {arg argActionStep;
		var holdModuleID, holdActionInd, holdActionText, holdVal1, holdVal2, holdVal3, holdVal4, holdGuiUpd;
		holdModuleID = argActionStep.at(0);
		holdActionInd = argActionStep.at(1);
		holdVal1 = argActionStep.at(2);
		holdVal2 = argActionStep.at(3);
		holdVal3 = argActionStep.at(4);
		holdVal4 = argActionStep.at(5);
		holdActionText = argActionStep.at(7);
		holdGuiUpd = argActionStep.at(8);
		this.performAction(holdModuleID, holdActionInd, holdActionText, holdVal1, holdVal2, holdVal3, holdVal4, holdGuiUpd);
	}

	performAction {arg holdModuleID, holdActionInd, holdActionText, holdVal1, holdVal2, holdVal3, holdVal4, holdGuiUpd=0,
		ignoreLatency = false, mapValues = false;
		var holdModule, holdArrActionItems, holdAction, holdLatency, errorFound, holdControlSpec;
		holdModule = system.getModuleFromID(holdModuleID);

		errorFound = false;
		// make sure that inf loop isn't created by sync start calling itself
		if ( holdModule == system
			and: (holdActionText == "Sync Start")
			and: (parentModule.getSynthArgSpec("syncStart") == 1), {
				TXInfoScreen.new("WARNING: " ++ this.instName ++
					"  cannot run the action Sync Start since this would cause an infinite loop.  Seq has been stopped."
				);
				errorFound = true;
				this.stopSequencer;
		});
		if ( errorFound != true and: (holdModule != 0), {
			holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName.asSymbol;});
			// if text found, match action string with text, else use numerical value
			if (holdActionText.notNil, {
				holdActionInd = holdArrActionItems.indexOf(holdActionText.asSymbol) ? holdActionInd;
				holdAction = holdModule.arrActionSpecs.at(holdActionInd);
			},{
				// if text not found, use number but only select older actions with legacyType == 1
				holdAction = holdModule.arrActionSpecs
				.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
			});
			if (holdAction.notNil, {
				// if module is another sequencer, don't use latency (since they already use latency)
				if (ignoreLatency or: {system.arrAllPossCurSeqModules.indexOfEqual(holdModule.class).notNil}, {
					holdLatency = 0;
				},{
					holdLatency = system.seqLatency;
				});
				if (mapValues == true, {
					if (holdAction.guiObjectType == \number, {
						if (holdAction.arrControlSpecFuncs.asArray[0].notNil && holdVal1.notNil, {
							holdControlSpec = holdAction.arrControlSpecFuncs[0].value;
						});
					});
					if (holdAction.guiObjectType == \popup, {
						holdControlSpec = ControlSpec(0, holdAction.getItemsFunction.value.size - 1, step: 1);
					});
					if (holdAction.guiObjectType == \checkbox, {
						holdControlSpec = ControlSpec(0, 1, step: 1);
					});
					if (holdControlSpec.notNil && holdVal1.notNil, {
						holdVal1 = holdControlSpec.map(holdVal1);
					});
					if (holdAction.arrControlSpecFuncs.asArray[1].notNil && holdVal2.notNil, {
						holdVal1 = holdAction.arrControlSpecFuncs[1].value.map(holdVal2);
					});
					if (holdAction.arrControlSpecFuncs.asArray[2].notNil && holdVal3.notNil, {
						holdVal1 = holdAction.arrControlSpecFuncs[2].value.map(holdVal3);
					});
					if (holdAction.arrControlSpecFuncs.asArray[3].notNil && holdVal4.notNil, {
						holdVal1 = holdAction.arrControlSpecFuncs[3].value.map(holdVal4);
					});
				});
				// use bundle to allow for latency
				system.server.makeBundle(holdLatency, {
					// if action type is commandAction then value it with arguments
					if (holdAction.actionType == \commandAction, {
						holdAction.actionFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
					});
					// if action type is valueAction then value it with arguments
					if (holdAction.actionType == \valueAction, {
						holdAction.setValueFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
					});
					// gui update
					if (holdGuiUpd == 1, {
						system.flagGuiUpd;
					});
				});
			});
			// screen update
			system.flagGuiIfModDisplay(holdModule);
		});
	}

	makeCopyNoteData {arg track;
		// store copy in class as event
		parentModule.class.classData.copyNoteData = ();
		parentModule.class.classData.copyNoteData.selectionStart = track.selectionStart.copy;
		parentModule.class.classData.copyNoteData.selectionEnd = track.selectionEnd.copy;
		//parentModule.class.classData.copyNoteData.selectionIndices = track.selectionIndices;
		parentModule.class.classData.copyNoteData.selectionNotes = track.arrNotes.at(track.selectionIndices).deepCopy;
		//OLD: parentModule.class.classData.copyNoteData.selectionNotes = track.arrEditNotes.asArray;
	}

	makeLocalCopyNoteData {arg track;
		// store copy in class as event
		track.localCopyNoteData = ();
		track.localCopyNoteData.selectionStart = track.selectionStart.copy;
		track.localCopyNoteData.selectionEnd = track.selectionEnd.copy;
		//track.localCopyNoteData.selectionIndices = track.selectionIndices;
		track.localCopyNoteData.selectionNotes = track.arrNotes.at(track.selectionIndices).deepCopy;
		//OLD: track.localCopyNoteData.selectionNotes = track.arrEditNotes.asArray;
	}

	deleteSelectedNoteVals {arg track, probability = 1;
		// remove items
		track.selectionIndices.sort.reverse.do({arg ind, i;
			if (probability.coin, {
				track.arrNotes.removeAt(ind);
			});
		});
		track.selectionIndices = [];
		track.arrEditNotes = [];
		this.reindexNoteTimes(track);
		parentModule.calculateEndTime;
	}

	deleteAllNotesInSelectionRange {arg track;
		this.rebuildEditNotesFromSelectionStartEnd(track);
		this.rebuildSelectionIndicesFromEditNotes(track);
		this.deleteSelectedNoteVals(track);
	}

	shiftNotesFrom {arg track, startTimeBeats, shiftBeats;
		var holdIndex, testFunction, bodyFunction;
		if ((track.arrTimes.size > 0) and: {(startTimeBeats <= track.arrTimes.last)}, {
			holdIndex = track.arrTimes.indexOf(startTimeBeats);
			if (holdIndex.isNil, {
				holdIndex = track.arrTimes.indexInBetween(startTimeBeats).roundUp;
			});
			if (holdIndex.notNil, {
				testFunction = {holdIndex < track.arrNotes.size};
				bodyFunction = {var holdTime;
					holdTime = track.arrNotes[holdIndex][0] + shiftBeats;
					// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
					track.arrNotes[holdIndex].put(0, holdTime);
					track.arrNotes[holdIndex].put(4, holdTime + track.arrNotes[holdIndex][3]);
					holdIndex = holdIndex + 1;
				};
				while ( testFunction, bodyFunction );
			});
			this.reindexNoteTimes(track);
		});
	}

	pasteCopyNoteData {arg track, startTimeBeats, endTimeBeats;
		var deltaNotes;
		var tempArray, copies, holdIndex, copySelectionTime;

		if ((parentModule.class.classData.copyNoteData.selectionNotes.asArray.size > 0) && (endTimeBeats > startTimeBeats), {
			copySelectionTime = parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart;
			if ((parentModule.class.classData.copyNoteData.size > 0 ) && (copySelectionTime > 0), {
				deltaNotes = parentModule.class.classData.copyNoteData.selectionNotes.collect({arg item, i;
					var holdTime = item[0] - parentModule.class.classData.copyNoteData.selectionStart;
					var holdNoteData = item.deepCopy;
					[holdTime, item[1], item[2], item[3]];
				});
				tempArray = [];
				copies = ((endTimeBeats - startTimeBeats) / copySelectionTime).ceil;
				copies.do({arg ind;
					var timeOffset = (ind * copySelectionTime) + startTimeBeats;
					deltaNotes.do({arg item, i;
						var holdTime = item[0] + timeOffset;
						if (holdTime < endTimeBeats, {
							// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
							tempArray = tempArray.add([holdTime, item[1], item[2], item[3], holdTime + item[3]]);
						});
					});
				});
				if (tempArray.size > 0, {
					track.selectionStart = startTimeBeats;
					track.selectionEnd = endTimeBeats;
					this.mergeNewNoteOnOffs(track, tempArray);
					track.arrEditNotes = tempArray;
					this.rebuildSelectionIndicesFromEditNotes(track);
				});
			});
		});
	}

	pasteLocalCopyNoteData {arg track, startTimeBeats, endTimeBeats;
		var deltaNotes;
		var tempArray, copies, holdIndex, copySelectionTime;

		if ((track.localCopyNoteData.selectionNotes.asArray.size > 0) && (endTimeBeats > startTimeBeats), {
			copySelectionTime = track.localCopyNoteData.selectionEnd - track.localCopyNoteData.selectionStart;
			if ((track.localCopyNoteData.size > 0 ) && (copySelectionTime > 0), {
				deltaNotes = track.localCopyNoteData.selectionNotes.collect({arg item, i;
					var holdTime = item[0] - track.localCopyNoteData.selectionStart;
					var holdNoteData = item.deepCopy;
					[holdTime, item[1], item[2], item[3]];
				});
				tempArray = [];
				copies = ((endTimeBeats - startTimeBeats) / copySelectionTime).ceil;
				copies.do({arg ind;
					var timeOffset = (ind * copySelectionTime) + startTimeBeats;
					deltaNotes.do({arg item, i;
						var holdTime = item[0] + timeOffset;
						if (holdTime < endTimeBeats, {
							// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
							tempArray = tempArray.add([holdTime, item[1], item[2], item[3], holdTime + item[3]]);
						});
					});
				});
				if (tempArray.size > 0, {
					track.selectionStart = startTimeBeats;
					track.selectionEnd = endTimeBeats;
					this.mergeNewNoteOnOffs(track, tempArray);
					track.arrEditNotes = tempArray;
					this.rebuildSelectionIndicesFromEditNotes(track);
				});
			});
		});
	}

	pasteStretchCopyNoteData {arg track, startTimeBeats, endTimeBeats, stretchDuration;
		var stretchFactor, arrStretchedNotes, orig_selectionNotes, orig_selectionStart, orig_selectionEnd;
		if ((parentModule.class.classData.copyNoteData.selectionNotes.asArray.size > 0) && (endTimeBeats > startTimeBeats), {
			// store original values
			orig_selectionNotes = parentModule.class.classData.copyNoteData.selectionNotes;
			orig_selectionEnd = parentModule.class.classData.copyNoteData.selectionEnd;
			// stretch copied notes
			stretchFactor = (endTimeBeats - startTimeBeats) / (parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart);
			arrStretchedNotes = parentModule.class.classData.copyNoteData.selectionNotes.collect({arg item, i;
				// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
				var startDelta = item[0] - parentModule.class.classData.copyNoteData.selectionStart;
				var startTime = parentModule.class.classData.copyNoteData.selectionStart + (startDelta * stretchFactor);
				var duration;
				if (stretchDuration == true, {
					duration = item[3] * stretchFactor;
				},{
					duration = item[3];
				});
				[startTime, item[1], item[2], duration, startTime + duration];
			});
			parentModule.class.classData.copyNoteData.selectionNotes = arrStretchedNotes;
			// stretch copied selectionEnd
			parentModule.class.classData.copyNoteData.selectionEnd = parentModule.class.classData.copyNoteData.selectionStart +
			((parentModule.class.classData.copyNoteData.selectionEnd - parentModule.class.classData.copyNoteData.selectionStart) * stretchFactor);
			// paste
			this.pasteCopyNoteData(track, startTimeBeats, endTimeBeats);
			// restore original values (before stretch) to clipboard
			parentModule.class.classData.copyNoteData.selectionNotes = orig_selectionNotes;
			parentModule.class.classData.copyNoteData.selectionEnd = orig_selectionEnd;
		});
	}

	transposeEditNotes {arg track;
		var transpose;
		transpose = (track.processVar1 * 12) + track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[1] = (holdNoteData[1] + transpose).max(0).min(127);
		});
	}

	timeShiftEditNotes {arg track;
		var timeShift;
		timeShift = (track.processVar1 * parentModule.getSynthArgSpec("beatsPerBar")) + track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[0] = (holdNoteData[0] + timeShift).max(0);
			holdNoteData[4] = (holdNoteData[4] + timeShift).max(0);
		});
	}

	timeShiftSelectionStartEnd {arg track;
		var timeShift;
		timeShift = (track.processVar1 * parentModule.getSynthArgSpec("beatsPerBar")) + track.processVar2;
		track.selectionStart = (track.selectionStart + timeShift).max(0);
		track.selectionEnd = (track.selectionEnd + timeShift).max(0);
	}

	quantiseEditNotesStart {arg track;
		var strength, swing, startQuant, startQuantSwung, isEvenSnap;
		strength = track.processVar1;
		swing = track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			startQuant = holdNoteData[0].round(track.snapBeats);
			isEvenSnap = (startQuant / track.snapBeats) % 2;
			startQuantSwung = startQuant + (track.snapBeats * 0.5 * swing * isEvenSnap);
			holdNoteData[0] = holdNoteData[0].blend(startQuantSwung, strength);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	quantiseEditNotesStartEnd {arg track;
		var strength, swing, startQuant, startQuantSwung, endQuant, endQuantSwung, holdEnd, isEvenSnap;
		strength = track.processVar1;
		swing = track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			startQuant = holdNoteData[0].round(track.snapBeats);
			isEvenSnap = (startQuant / track.snapBeats) % 2;
			startQuantSwung = startQuant + (track.snapBeats * 0.5 * swing * isEvenSnap);
			endQuant = holdNoteData[4].round(track.snapBeats);
			isEvenSnap = (endQuant / track.snapBeats) % 2;
			endQuantSwung = endQuant + (track.snapBeats * 0.5 * swing * isEvenSnap);
			holdNoteData[0] = holdNoteData[0].blend(startQuantSwung, strength);
			holdEnd = holdNoteData[4].blend(endQuantSwung, strength);
			holdNoteData[3] = (holdEnd - holdNoteData[0]).max(0.01);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	quantiseEditNotesDuration {arg track;
		var strength;
		strength = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[3] = holdNoteData[3].blend(holdNoteData[3].round(track.snapBeats), strength);
			holdNoteData[3] = holdNoteData[3].max(0.01);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	legatoiseEditNotesDuration {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var minLength, maxLength, arrSortedStartTimes, noteLength, holdIndex;
		arrSortedStartTimes = track.arrEditNotes.asArray.collect({arg holdNoteData, i; holdNoteData[0]});
		arrSortedStartTimes = arrSortedStartTimes.asSet.asArray.sort;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			holdIndex = arrSortedStartTimes.indexOf(holdNoteData[0]);
			// ignore last note
			if (holdIndex < (arrSortedStartTimes.size - 1), {
				minLength = track.processVar1;
				maxLength = track.processVar2;
				noteLength = (arrSortedStartTimes[holdIndex + 1] - arrSortedStartTimes[holdIndex]).max(minLength).min(maxLength);
				holdNoteData[3] = noteLength;
				holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
			});
		});
	}

	removeEditNoteOverlaps{arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var minLength, maxLength, arrSortedStartTimes, holdIndex;
		arrSortedStartTimes = track.arrEditNotes.asArray.collect({arg holdNoteData, i; holdNoteData[0]});
		arrSortedStartTimes = arrSortedStartTimes.asSet.asArray.sort;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			holdIndex = arrSortedStartTimes.indexOf(holdNoteData[0]);
			// ignore last note
			if (holdIndex < (arrSortedStartTimes.size - 1), {
				minLength = track.processVar1;
				maxLength = arrSortedStartTimes[holdIndex + 1] - arrSortedStartTimes[holdIndex];
				holdNoteData[3] = holdNoteData[3].max(minLength).min(maxLength);
				holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
			});
		});
	}

	scaleEditNotesStartDuration {arg track;
		var scale, startDelta;
		scale = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			startDelta = (holdNoteData[0] - track.selectionStart) * scale;
			holdNoteData[0] = track.selectionStart + startDelta;
			holdNoteData[3] = (holdNoteData[3] * scale).max(0.01);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	scaleEditNotesStart {arg track;
		var scale, startDelta;
		scale = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			startDelta = (holdNoteData[0] - track.selectionStart) * scale;
			holdNoteData[0] = track.selectionStart + startDelta;
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	scaleEditNotesDuration {arg track;
		var scale = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[3] = (holdNoteData[3] * scale).max(0.01);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	scaleSelectionEnd {arg track;
		var scale = track.processVar1;
		track.selectionEnd = track.selectionStart + ((track.selectionEnd - track.selectionStart) * scale);
	}

	scaleEditNotesVelocity {arg track;
		var scale = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[2] = (holdNoteData[2] * scale).clip(0, 127);
		});
	}

	scaleEditNotesVelocityToRange {arg track;
		var min = track.processVar1;
		var max = track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[2] = holdNoteData[2].linlin(0, 127, 1.27 * min, 1.27 * max);
		});
	}

	quantiseEditNotesVelocity {arg track;
		var quantSize = 1 / (track.processVar1 + 1);
		var strength = track.processVar2;
		var velQuant;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			velQuant = (holdNoteData[2]/127).round(quantSize).max(quantSize) * 127;
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[2] = holdNoteData[2].blend(velQuant, strength);
		});
	}

	// fade
	fadeEditNotesVelocity {arg track, curve;
		var startVel = track.processVar1 * 1.27; // convert to 0-127 midi range
		var endVel = track.processVar2 * 1.27;
		var startTime, endTime, proportion, mappedProp, arrTimes;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		if (track.arrEditNotes.size == 1, {
			track.arrEditNotes[0][2] = endVel;
		}, {
			arrTimes = track.arrEditNotes.collect({arg item; item[0]});
			startTime = arrTimes.minItem;
			endTime = arrTimes.maxItem;
			track.arrEditNotes.asArray.do({arg holdNoteData, i;
				if (holdNoteData[0] == startTime, {
					proportion = 0;

				}, {
					if (holdNoteData[0] == endTime, {
						proportion = 1;

					}, {
						proportion = (holdNoteData[0] - startTime) / (endTime - startTime);
						proportion = proportion.clip(0,1);
					});
				});
				case
				{curve == "linear"} {
					mappedProp = proportion;
				}
				{curve == "exp"} {
					mappedProp = TXLineWarp.getLineWarpVal(proportion, 1);
				}
				{curve == "-exp"} {
					mappedProp = TXLineWarp.getLineWarpVal(proportion, 3);
				}
				{curve == "1"} {
					mappedProp = Env([0, 1], [1], 1).at(proportion);
				}
				{curve == "-1"} {
					mappedProp = Env([0, 1], [1], -1).at(proportion);
				}
				{curve == "2"} {
					mappedProp = Env([0, 1], [1], 2).at(proportion);
				}
				{curve == "-2"} {
					mappedProp = Env([0, 1], [1], -2).at(proportion);
				}
				;
				holdNoteData[2] = startVel + (mappedProp * (endVel - startVel));
			});
		});
	}

	// humanise

	humaniseEditNotesVelocity {arg track;
		var humanise = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var randVel = gauss(0.0, humanise * 0.33).clip2(humanise) * 1.27; // convert to 0-127 midi range
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[2] = (holdNoteData[2] + randVel).clip(0, 127);
		});
	}

	humaniseEditNotesStartDuration {arg track, positiveOnly;
		var humaniseStart = track.processVar1;
		var humaniseDur = track.processVar2;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var randStart = gauss(0.0, (humaniseStart * 0.33)).clip2(humaniseStart - 0.001);
			var randDur = gauss(0.0, (humaniseDur * 0.33)).clip2(humaniseDur - 0.001);
			if (positiveOnly == true, {
				randStart = randStart.abs;
				randDur = randDur.abs;
			});
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[0] = holdNoteData[0] + randStart;
			holdNoteData[3] = (holdNoteData[3] + randDur).max(0.01);
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	humaniseEditNotesStart {arg track, positiveOnly;
		var humaniseStart = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var randStart = gauss(0.0, (humaniseStart * 0.33)).clip2(humaniseStart - 0.001);
			if (positiveOnly == true, {
				randStart = randStart.abs;
			});
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[0] = holdNoteData[0] + randStart;
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	humaniseEditNotesDuration {arg track, positiveOnly;
		var humaniseDur = track.processVar1;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var randDur = gauss(0.0, (humaniseDur * 0.33)).clip2(humaniseDur - 0.001);
			if (positiveOnly == true, {
				randDur = randDur.abs;
			});
			// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
			holdNoteData[3] = holdNoteData[3] + randDur;
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
		});
	}

	// reverse

	reverseEditNotesStart {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var newArray = [];
		track.arrEditNotes.asArray.reverseDo({arg item, i;
			var holdNoteData = item.deepCopy;
			var holdEnd = holdNoteData[4].min(track.selectionEnd);
			var holdDelta = track.selectionEnd - holdEnd;
			holdNoteData[0] = track.selectionStart + holdDelta;
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
			newArray = newArray.add(holdNoteData);
		});
		track.arrEditNotes = newArray;
	}

	reverseEditNotesPitch {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var arrPitches = track.arrEditNotes.asArray.collect({arg item, i; item[1]});
		track.arrEditNotes.asArray.reverseDo({arg holdNoteData, i;
			var holdPitch = arrPitches[i];
			holdNoteData[1] = holdPitch;
		});
	}

	reverseEditNotesVelocity {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var newArray = [];
		var arrVelocities = track.arrEditNotes.asArray.collect({arg item, i; item[2]});
		track.arrEditNotes.asArray.reverseDo({arg holdNoteData, i;
			var holdVelocity = arrVelocities[i];
			holdNoteData[2] = holdVelocity;
		});
	}


	// scramble order

	scrambleOrderEditNotesStart {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var newArray = [];
		var arrTimes = track.arrEditNotes.asArray.collect({arg item, i; item[0]});
		arrTimes = arrTimes.scramble;
		track.arrEditNotes.asArray.reverseDo({arg item, i;
			var holdNoteData = item.deepCopy;
			holdNoteData[0] = arrTimes[i];
			holdNoteData[4] = holdNoteData[0] + holdNoteData[3];
			newArray = newArray.add(holdNoteData);
		});
		newArray.sort({ arg a, b; a[0] < b[0]; });
		track.arrEditNotes = newArray;
	}

	scrambleOrderEditNotesPitch {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var arrPitches = track.arrEditNotes.asArray.collect({arg item, i; item[1]});
		arrPitches = arrPitches.scramble;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var holdPitch = arrPitches[i];
			holdNoteData[1] = holdPitch;
		});
	}

	scrambleOrderEditNotesVelocity {arg track;
		// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
		var arrVelocities = track.arrEditNotes.asArray.collect({arg item, i; item[2]});
		arrVelocities = arrVelocities.scramble;
		track.arrEditNotes.asArray.do({arg holdNoteData, i;
			var holdVelocity = arrVelocities[i];
			holdNoteData[2] = holdVelocity;
		});
	}

	//// Multi track methods ////////////////////////////////


	getMultiTrackProcesses {
		^[
			["...", {
				//xxx;
			}, [], []],
			//}, ["xxx", "yyy"], [ControlSpec(0.1,1000, 'exp', default: 1), ControlSpec(0, 1, default: 0)]],
			["copy selection for all tracks", {
				this.copySelForAllTracks;
				system.showView;
			}, [], []],
			["cut selection & leave space in selected tracks", {
				this.deleteSelForSelTracks;
				system.showView;
			}, [], []],
			["cut selection & remove space in selected tracks", {
				this.deleteSelForSelTracks;
				this.shiftRemoveSelForSelTracks;
				system.showView;
			}, [], []],
			["delete selected markers", {
				this.deleteSelectedMarkers;
				system.showView;
			}, [], []],

			["hide  & unselect muted tracks", {
				this.hideUnselMutedTracks;
				system.showView;
			}, [], []],
			["hide & unselect selected tracks", {
				this.hideUnselSelectedTracks;
				system.showView;
			}, [], []],
			["hide unselected tracks", {
				this.hideUnSelectedTracks;
				system.showView;
			}, [], []],
			["interpolate selection in selected ctrl tracks, add points on track snap grid", {
				this.interpolateSelectedControlTracks(parentModule.getSynthArgSpec("actionVar1").asInteger,
					parentModule.getSynthArgSpec("actionVar1").asInteger);
				system.showView;
			}, ["curve"], [TXLineWarp.getLineWarpStrings]],
			["interpolate +/- selection in selected ctrl tracks, add points on track snap grid", {
				this.interpolateSelectedControlTracks(parentModule.getSynthArgSpec("actionVar1").asInteger,
					parentModule.getSynthArgSpec("actionVar2").asInteger);
				system.showView;
			}, ["curve +", "curve -"], [TXLineWarp.getLineWarpStrings, TXLineWarp.getLineWarpStrings]],
			["insert space in selected tracks", {
				this.insertSpaceInSelTracks;
				system.showView;
			}, [], []],
			["insert value of output 1 at selection start in selected controller tracks", {
				this.writeKeyframeInSelCtrlTracks(true);
				system.showView;
			}, [], []],
			["set direct inputs to output 1 values in selected controller tracks", {
				this.setDirectInputToOut1ValInSelCtrlTracks;
			}, [], []],
			["move selected markers", {
				this.moveSelectedMarkers(parentModule.getSynthArgSpec("actionVar1"));
				system.showView;
			}, ["Timeshift beats"], [ControlSpec(-40, 40, default: 0)]],
			// ["mute all tracks", {
			// 	this.muteAllTracks(1);
			// 	system.showView;
			// }, [], []],
			// ["mute no tracks", {
			// 	this.muteAllTracks(0);
			// 	system.showView;
			// }, [], []],
			["open MIDI file & import tracks", {
				this.openMidiFile;
				system.showView;
			}, [], []],
			["output values at current play time for selected controller tracks", {
				this.outputCurrValInSelCtrlTracks;
				system.showView;
			}, [], []],
			["overwrite selection with value of output 1 in selected controller tracks", {
				this.writeKeyframeInSelCtrlTracks(false);
				system.showView;
			}, [], []],
			["paste add for selected tracks", {
				this.pasteOverForSelTracks;
				system.showView;
			}, [], []],
			["paste insert for selected tracks", {
				this.pasteInsertForSelTracks;
				system.showView;
			}, [], []],
			["paste replace for selected tracks", {
				this.pasteReplaceForSelTracks;
				system.showView;
			}, [], []],
			["redo edit for selected tracks", {
				this.redoEditForSelTracks;
				system.showView;
			}, [], []],
			["save selected tracks to a MIDI file", {
				this.saveMidiFile;
				system.showView;
			}, [], []],
			// ["select all tracks", {
			// 	this.selectAllTracks(1);
			// 	system.showView;
			// }, [], []],
			// ["select no tracks", {
			// 	this.selectAllTracks(0);
			// 	system.showView;
			// }, [], []],
			["set monitor off for selected tracks", {
				this.setMonitorForSelTracks(0);
				system.showView;
			}, [], []],
			["set monitor on for selected tracks", {
				this.setMonitorForSelTracks(1);
				system.showView;
			}, [], []],
			["set record off for selected tracks", {
				this.setRecordForSelTracks(0);
				system.showView;
			}, [], []],
			["set record on for selected tracks", {
				this.setRecordForSelTracks(1);
				system.showView;
			}, [], []],
			["set mute off for selected tracks", {
				this.muteSelectedTracks(0);
				system.showView;
			}, [], []],
			["set mute on for selected tracks", {
				this.muteSelectedTracks(1);
				system.showView;
			}, [], []],
			["smooth selected controller tracks", {
				this.smoothValsForSelCtrlTracks();
			}, ["Smoothings"], [ControlSpec(1, 10, step: 1, default: 1)]],
			["swap muted/unmuted tracks", {
				this.swapMutedUnmutedTracks;
				system.showView;
			}, [], []],
			["swap selected/unselected tracks", {
				this.swapSelectedUnselectedTracks;
				system.showView;
			}, [], []],
			// ["thicken  selected controller tracks, add new points", {arg track;
			// 	this.thickenValsForSelCtrlTracks();
			// 	system.showView;
			// }, [], []],
			["thicken & interpolate selected controller tracks, add new points", {arg track;
				this.thickenInterpolateValsForSelCtrlTracks();
				system.showView;
			}, [], []],
			["thicken & smooth selected controller tracks, add new points", {arg track;
				this.thickenSmoothValsForSelCtrlTracks();
				system.showView;
			}, [], []],
			["undo edit for selected tracks", {
				this.undoEditForSelTracks;
				system.showView;
			}, [], []],
			// ["unhide all tracks", {
			// 	this.unhideAllTracks;
			// 	system.showView;
			// }, [], []],
		];
	}

	interpolateSelectedControlTracks {arg upInterpTypeIndex, downInterpTypeIndex;
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				// save history with edit
				this.doWithHistory(track, {
					// interpolate
					this.interpolateSelectedControllerVals(track, upInterpTypeIndex, downInterpTypeIndex);
					this.reindexControllerTimes(track);
					this.updateControllerSelectionIndices(track);
				});
			});
		});
	}

	selectAllTracks { arg setVal = 1;
		setVal = setVal.clip(0,1).round;
		parentModule.arrTracks.do({arg track, i;
			track.selected = setVal;
		});
	}

	swapSelectedUnselectedTracks {
		parentModule.arrTracks.do({arg  track, i;
			if (track.selected == 0, {
				track.selected = 1;
			}, {
				track.selected = 0;
			});
		});
	}

	muteAllTracks { arg  setVal = 1;
		setVal = setVal.clip(0,1).round;
		parentModule.arrTracks.do({arg track, i;
			track.mute = setVal;
		});
	}

	muteSelectedTracks { arg setVal = 1;
		setVal = setVal.clip(0,1).round;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				track.mute = setVal;
			});
		});
	}

	setMonitorForSelTracks { arg setVal = 1;
		setVal = setVal.clip(0,1).round;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				track.monitorInput = setVal;
				parentModule.buildTrackMonitorFuncs(track, setVal.asBoolean);
			});
		});
	}

	setRecordForSelTracks { arg setVal = 1;
		setVal = setVal.clip(0,1).round;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				track.recordArmed = setVal;
				parentModule.buildTrackRecordFuncs(track, setVal.asBoolean);
			});
		});
	}

	swapMutedUnmutedTracks {
		parentModule.arrTracks.do({arg track, i;
			if (track.mute == 0, {
				track.mute = 1;
			}, {
				track.mute = 0;
			});
		});
	}

	setSelectionForAllTracks {
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		parentModule.arrTracks.do({arg track, i;
			track.selectionStart = start;
			track.selectionEnd = end;
			if (track.trackType == 'Note', {
				this.rebuildEditNotesFromSelectionStartEnd(track);
				this.rebuildSelectionIndicesFromEditNotes(track);
			});
			if (track.trackType == 'Controller', {
				this.updateControllerSelectionIndices(track);
			});
		});
	}

	setSelectionForSelTracks {arg forNoteTracks = true, forControllerTracks = true;
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				track.selectionStart = start;
				track.selectionEnd = end;
				if (track.trackType == 'Note' && (forNoteTracks == true), {
					this.rebuildEditNotesFromSelectionStartEnd(track);
					this.rebuildSelectionIndicesFromEditNotes(track);
				});
				if (track.trackType == 'Controller' && (forControllerTracks == true), {
					this.updateControllerSelectionIndices(track);
				});
			});
		});
	}

	copySelForAllTracks {
		this.setSelectionForAllTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.trackType == 'Note', {
				//this.makeCopyNoteData(track);
				this.makeLocalCopyNoteData(track);
			});
			if (track.trackType == 'Controller', {
				//this.makeCopyControllerData(track);
				this.makeLocalCopyControllerData(track);
			});
		});
	}

	undoEditForSelTracks {
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				this.undoEdit(track);
			});
		});
	}

	redoEditForSelTracks{
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				this.redoEdit(track);
			});
		});
	}

	deleteSelForSelTracks {
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				if (track.trackType == 'Note', {
					// save history with edit
					this.doWithHistory(track, {
						this.deleteSelectedNoteVals(track);
					});
				});
				if (track.trackType == 'Controller', {
					// save history with edit
					this.doWithHistory(track, {
						this.deleteSelectedControllerVals(track);
					});
				});
			});
		});
	}

	deleteSelectedMarkers {
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		parentModule.dataBank.arrMarkerTimes.asArray.do({arg argTime, i;
			if ((argTime >= start) && (argTime < end), {
				parentModule.dataBank.arrMarkers[argTime] = nil;
			});
		});
		parentModule.rebuildArrMarkerTimes;
	}

	moveSelectedMarkers {arg shiftBeats;
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		var holdNewMarkers;
		holdNewMarkers = ();
		parentModule.dataBank.arrMarkerTimes.asArray.do({arg argTime, i;
			if ((argTime >= start) && (argTime < end), {
				if ((argTime + shiftBeats) > 0, {
					holdNewMarkers[argTime + shiftBeats] = parentModule.dataBank.arrMarkers[argTime];
				});
				parentModule.dataBank.arrMarkers[argTime] = nil;
			});
		});
		parentModule.dataBank.arrMarkers = parentModule.dataBank.arrMarkers ++ holdNewMarkers;
		parentModule.rebuildArrMarkerTimes;
	}

	shiftRemoveSelForSelTracks {
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				if (track.trackType == 'Note', {
					// save history with edit
					this.doWithHistory(track, {
						this.shiftNotesFrom(track, track.selectionEnd, track.selectionStart - track.selectionEnd);
					});
				});
				if (track.trackType == 'Controller', {
					// save history with edit
					this.doWithHistory(track, {
						this.shiftControllerValsFrom(track, track.selectionEnd, track.selectionStart - track.selectionEnd);
					});
				});

			});
		});
	}

	insertSpaceInSelTracks {
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				if (track.trackType == 'Note', {
					// save history with edit
					this.doWithHistory(track, {
						this.shiftNotesFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
					});
				});
				if (track.trackType == 'Controller', {
					// save history with edit
					this.doWithHistory(track, {
						this.shiftControllerValsFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
					});
				});

			});
		});
	}

	pasteInsertForSelTracks {   // Paste Insert Once at Selection Start
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				if (track.trackType == 'Note', {
					var pasteTime;
					if (track.localCopyNoteData.notNil, {
						if (track.localCopyNoteData.selectionEnd.notNil, {
							pasteTime = track.localCopyNoteData.selectionEnd - track.localCopyNoteData.selectionStart;
							// save history with edit
							this.doWithHistory(track, {
								// insert space & reindex before paste
								this.shiftNotesFrom(track, track.selectionStart, pasteTime);
								this.pasteLocalCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
							});
						});
					});
				});
				if (track.trackType == 'Controller', {
					var pasteTime;
					if (track.localCopyControllerData.notNil, {
						if (track.localCopyControllerData.selectionEnd.notNil, {
							pasteTime = track.localCopyControllerData.selectionEnd - track.localCopyControllerData.selectionStart;
							// save history with edit
							this.doWithHistory(track, {
								// insert space & before paste
								this.shiftControllerValsFrom(track, track.selectionStart, pasteTime);
								this.pasteLocalControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
							});
						});
					});
				});
			});
		});
	}

	pasteOverForSelTracks {  arg replace = false;
		this.setSelectionForSelTracks;
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				if (track.trackType == 'Note', {
					// Paste Add Once at Selection Start
					var pasteTime;
					if (track.localCopyNoteData.notNil, {
						if (track.localCopyNoteData.selectionEnd.notNil, {
							if (replace, {
								// save history with edit
								this.doWithHistory(track, {
									this.deleteSelectedNoteVals(track);
								});
							});
							pasteTime = track.localCopyNoteData.selectionEnd - track.localCopyNoteData.selectionStart;
							this.pasteLocalCopyNoteData (track, track.selectionStart, track.selectionStart + pasteTime);
						});
					});
				});
				if (track.trackType == 'Controller', {
					//Paste Over Once at Selection Start
					var pasteTime;
					if (track.localCopyControllerData.notNil, {
						if (track.localCopyControllerData.selectionEnd.notNil, {
							pasteTime = track.localCopyControllerData.selectionEnd -
							track.localCopyControllerData.selectionStart;
							// save history with edit
							this.doWithHistory(track, {
								this.pasteLocalControllerVals (track, track.selectionStart, track.selectionStart + pasteTime);
							});
						});
					});
				});
			});
		});
	}

	pasteReplaceForSelTracks {
		this.pasteOverForSelTracks(replace: true);
	}

	smoothValsForSelCtrlTracks {
		var smoothings = parentModule.getSynthArgSpec("actionVar1");
		this.setSelectionForSelTracks (forNoteTracks: false, forControllerTracks: true);
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				// save history with edit
				this.doWithHistory(track, {
					smoothings.asInteger.do({this.smoothSelectedControllerVals(track);});
				});
			});
		});
	}

	thickenValsForSelCtrlTracks {
		this.setSelectionForSelTracks (forNoteTracks: false, forControllerTracks: true);
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				// save history with edit
				this.doWithHistory(track, {
					this.thickenSelectedControllerVals(track, interpolate: false);
					this.reindexControllerTimes(track);
					this.updateControllerSelectionIndices(track);
				});
			});
		});
	}

	thickenInterpolateValsForSelCtrlTracks {
		this.setSelectionForSelTracks (forNoteTracks: false, forControllerTracks: true);
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				// save history with edit
				this.doWithHistory(track, {
					this.thickenSelectedControllerVals(track, interpolate: true);
					this.reindexControllerTimes(track);
					this.updateControllerSelectionIndices(track);
				});
			});
		});
	}

	thickenSmoothValsForSelCtrlTracks {
		this.setSelectionForSelTracks (forNoteTracks: false, forControllerTracks: true);
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				// save history with edit
				this.doWithHistory(track, {
					this.thickenSelectedControllerVals(track, interpolate: true);
					this.reindexControllerTimes(track);
					this.updateControllerSelectionIndices(track);
					this.smoothSelectedControllerVals(track);
				});
			});
		});
	}

	writeKeyframeInSelCtrlTracks { arg insertSpaceFirst = false;
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				track.selectionStart = start;
				track.selectionEnd = end;
				// save history with edit
				this.doWithHistory(track, {
					this.writeKeyframeInTrack (track, insertSpaceFirst);
				});
			});
		});
	}

	setDirectInputToOut1ValInSelCtrlTracks {
		parentModule.arrTracks.do({arg track, i;
			if ((track.selected == 1) && (track.trackType == 'Controller'), {
				track.directInputVal = this.getCurrentOut1ValueForTrack(track);
			});
		});
	}

	outputCurrValInSelCtrlTracks {
		var start = parentModule.getSynthArgSpec("multiSelectionStart");
		var end = parentModule.getSynthArgSpec("multiSelectionEnd");
		var holdIndex;
		parentModule.arrTracks.do({arg track, i;
			var schedTimeBeats, holdIndex;
			// 6 outputs
			6.do({arg count;
				var numString = (count + 1).asString;
				var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
				var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
				var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
				var isActive, isFirstEvent;
				if (count == 0, {
					isActive = (track[outputActiveKey] == 1) || (track.outputMidiActive == 1);
				},{
					isActive = (track[outputActiveKey] == 1);
				});
				if ((track[moduleIDKey] != 0) && (isActive == true) && (track.mute.asBoolean != true), {
					if ((track.selected == 1) && (track.trackType == 'Controller'), {
						if (track.arrControllerVals.size > 0, {
							// get start index
							holdIndex = track.arrTimes.indexOf(parentModule.currentPlayTime);
							if (holdIndex.isNil, {
								// get preceding event
								holdIndex = max(track.arrTimes.indexInBetween(parentModule.currentPlayTime).floor, 0);
							});
							if (holdIndex < track.arrControllerVals.size, {
								var holdEvent;
								holdEvent = track.arrControllerVals[holdIndex];
								// output val
								this.performAction (track[moduleIDKey], 0, track[parameterNameKey], holdEvent[1], mapValues: true);
								// output midi
								if ((count == 0) && (track.outputMidiActive == 1), {  // only do once
									if (track.outputMidiPitchBend == 1, {
										parentModule.sendMIDIMessage(track, 'Pitchbend', outControlVal: holdEvent[1]);
									},{
										parentModule.sendMIDIMessage(track, 'Controller', outControlVal: holdEvent[1]);
									});
								});
							});
						});
					});
				});
			});
		});
	}

	writeKeyframeInTrack {arg track, insertSpaceFirst = false;
		//var outputActiveKey = ("output1Active").asSymbol;
		var holdModule, holdArrActionItems, holdActionInd, holdAction, holdActionText, holdControlSpec, holdVal;
		var holdDict;
		// add single value at select start by sampling output 1
		holdModule = system.getModuleFromID(track['output1ModuleID']);
		holdActionText = track.output1ParameterName;
		if (holdModule != 0, {
			holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName.asSymbol;});
			// if text found, match action string with text, else use numerical value
			if (holdActionText.notNil, {
				holdActionInd = holdArrActionItems.indexOf(holdActionText.asSymbol) ? 0;
				holdAction = holdModule.arrActionSpecs.at(holdActionInd);
			});
			if (holdAction.notNil, {
				if (holdAction.guiObjectType == \number, {
					if (holdAction.arrControlSpecFuncs.asArray[0].notNil, {
						holdControlSpec = holdAction.arrControlSpecFuncs[0].value;
					});
				});
				if (holdAction.guiObjectType == \popup, {
					holdControlSpec = ControlSpec(0, holdAction.getItemsFunction.value.size - 1, step: 1);
				});
				if (holdAction.guiObjectType == \checkbox, {
					holdControlSpec = ControlSpec(0, 1, step: 1);
				});
				holdVal = holdAction.getValueFunction.value;
				if (holdControlSpec.notNil && holdVal.notNil, {
					holdVal = holdControlSpec.unmap(holdVal);
				});
				if (holdVal.notNil, {
					// add sampled value at select start
					if (insertSpaceFirst, {
						this.shiftControllerValsFrom(track, track.selectionStart, track.selectionEnd - track.selectionStart);
					});
					track.arrEditControllerVals = [[track.selectionStart, holdVal]];
					// snap edits
					this.snapEditControllerValsToGrid(track, track.snapToGrid, track.quantizeValue);
					// merge & clear edits
					this.mergeEditControllerVals(track);
				});
			});
		});
	}

	getCurrentOut1ValueForTrack {arg track;
		var holdModule, holdActionText, holdArrActionItems, holdActionInd, holdAction, holdControlSpec, holdVal;
		var outVal = 0;
		// add single value at select start by sampling output 1
		holdModule = system.getModuleFromID(track['output1ModuleID']);
		holdActionText = track.output1ParameterName;
		if (holdModule != 0, {
			holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName.asSymbol;});
			// if text found, match action string with text, else use numerical value
			if (holdActionText.notNil, {
				holdActionInd = holdArrActionItems.indexOf(holdActionText.asSymbol) ? 0;
				holdAction = holdModule.arrActionSpecs.at(holdActionInd);
			});
			if (holdAction.notNil, {
				if (holdAction.guiObjectType == \number, {
					if (holdAction.arrControlSpecFuncs.asArray[0].notNil, {
						holdControlSpec = holdAction.arrControlSpecFuncs[0].value;
					});
				});
				if (holdAction.guiObjectType == \popup, {
					holdControlSpec = ControlSpec(0, holdAction.getItemsFunction.value.size - 1, step: 1);
				});
				if (holdAction.guiObjectType == \checkbox, {
					holdControlSpec = ControlSpec(0, 1, step: 1);
				});
				holdVal = holdAction.getValueFunction.value;
				if (holdControlSpec.notNil && holdVal.notNil, {
					holdVal = holdControlSpec.unmap(holdVal);
				});
				outVal = holdVal ? 0;
			});
		});
		^outVal;
	}

	getOut1SpecsForTrack {arg track;
		var holdModule, holdArrActionItems, holdActionInd, holdAction, holdActionText;
		var holdControlSpec, holdPopupItems, holdGuiObjectType;
		holdModule = system.getModuleFromID(track['output1ModuleID']);
		holdActionText = track.output1ParameterName;
		if (holdModule != 0, {
			holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName.asSymbol;});
			// if text found, match action string with text, else use numerical value
			if (holdActionText.notNil, {
				holdActionInd = holdArrActionItems.indexOf(holdActionText.asSymbol) ? 0;
				holdAction = holdModule.arrActionSpecs.at(holdActionInd);
			});
			if (holdAction.notNil, {
				holdGuiObjectType = holdAction.guiObjectType;
				if (holdGuiObjectType == \number, {
					if (holdAction.arrControlSpecFuncs.asArray[0].notNil, {
						holdControlSpec = holdAction.arrControlSpecFuncs[0].value;
					});
				}, {
					if (holdGuiObjectType == \popup, {
						holdPopupItems = holdAction.getItemsFunction.value;
						holdControlSpec = ControlSpec(0, holdPopupItems.size - 1, step: 1);
					}, {
						if (holdGuiObjectType == \checkbox, {
							holdControlSpec = ControlSpec(0, 1, step: 1);
						});
					});
				});
			});
		});
		^[holdControlSpec, holdPopupItems, holdGuiObjectType];
	}

	hideUnselMutedTracks {
		parentModule.arrTracks.do({arg track, i;
			if (track.mute == 1, {
				track.hidden = true;
				track.selected = 0;
			});
		});
	}

	hideUnselSelectedTracks {
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 1, {
				track.hidden = true;
				track.selected = 0;
			});
		});
	}

	hideUnSelectedTracks {
		parentModule.arrTracks.do({arg track, i;
			if (track.selected == 0, {
				track.hidden = true;
			});
		});
	}

	unhideAllTracks {
		parentModule.arrTracks.do({arg track, i;
			track.hidden = false;
		});
	}

	openMidiFile {
		var errFound, errorScreen;
		var newMIDIFile;
		errFound = 0;
		Dialog.openPanel({ arg newPath;
			newMIDIFile = SimpleMIDIFile.read(newPath);
			if (newMIDIFile.midiEvents.size > 0, {
				this.importMidiFile(newMIDIFile);
			},{
				errFound = 1;
			});
			// recreate view
			system.showView;
		}, {
				errFound = 1;
		});
		// Show error screen if error found
		if (errFound == 1, {
			errorScreen = TXInfoScreen.new("ERROR: invalid MIDI file: " ++ newMIDIFile.path);
		});
	}

	importMidiFile {arg newMIDIFile;
		var tempoMap = newMIDIFile.tempoMap;
		var metaEvents = newMIDIFile.metaEvents;
		var divisionRecip = newMIDIFile.division.reciprocal;	// division: number of ticks per quarter note
		var numMidiTracks = newMIDIFile.tracks;
		var bpmControlSpec = ControlSpec(1, 999);
		var recip127 = 127.reciprocal;
		var pbRecip = 16383.reciprocal;
		var arrEvents;

		// prep
		newMIDIFile.convertPitchBend;

		// add tempoTrack
		tempoMap = tempoMap.reject({arg item, i;
			// remove duplicates - use last event at any time
			(i !=  (tempoMap.size - 1)) and: {tempoMap[i][0] == tempoMap[i + 1][0]}
		});
		arrEvents = tempoMap.collect({arg item, i;
			// TX Controller Event format: [timeBeats, normalisedVal]
			[item[0] * divisionRecip,  bpmControlSpec.unmap(item[1])];
		});
		this.addCtrlTrackFromMidi("tempo track", 0, 0, 0, arrEvents);

		numMidiTracks.do({arg midiTrackNum;
			var midiTrackName;
			var midiNoteEvents, midiControllerEvents, midiPitchBendEvents;
			var noteGroups, controllerGroups, pitchBendGroups;

			midiTrackName = newMIDIFile.trackName(midiTrackNum);


			// MIDI DATA FORMAT
			// var <>midiEvents;    // format: [trackNumber, absTime, type, channel, val1, val2]
			// var <>metaEvents;  // format: [trackNumber, absTime, type, [ values ] / value / string ]

			// build other tracks using midinote/cc/pitchbend data
			// sort by:
			// Track no, Track type, Midi channel, Midi CC or: -1 (as relevent)
			// for each one create tracks with correct midi output settings: channel, CC
			// trackName - tracks should be named by Track name or TrN, channel, cc (if relevent)
			// e.g. Tr2.ch_11.cc_21

			// TX Note Event Format: [startTime, noteNum, vel, noteLength, endTime]
			// TX Controller Event format: [timeBeats, normalisedVal]

			// create note tracks
			midiNoteEvents = newMIDIFile.noteSustainEvents(nil, midiTrackNum);
			if (midiNoteEvents.size > 0, {
				// separate by channel
				noteGroups = midiNoteEvents.sort({arg a, b; a[3] < b[3]}).separate({arg a, b; a[3] != b[3]});
				noteGroups.do({arg arrMidiEvents, i;
					arrEvents = arrMidiEvents.collect({arg item, i;
						var startTime, noteLength;
						// noteSustainEvents format: [track, absTime, \noteOn, channel, note, velo, dur, upVelo]
						// TX Note Event format: [startTime, noteNum, vel, noteLength, endTime]
						startTime = item[1] * divisionRecip;
						noteLength = item[6] * divisionRecip;
						[startTime, item[4], item[5], noteLength, startTime + noteLength];
					});
					this.addNoteTrackFromMidi(midiTrackName, arrMidiEvents[0][3], arrEvents);
				});
			});
			// create PitchBend tracks
			midiPitchBendEvents = newMIDIFile.pitchBendEvents(nil, midiTrackNum);
			if (midiPitchBendEvents.size > 0, {
				pitchBendGroups = midiPitchBendEvents.sort({arg a, b; a[3] < b[3]}).separate({arg a, b; a[3] != b[3]});
				pitchBendGroups.do({arg arrMidiEvents, i;
					var nameString;
					arrEvents = arrMidiEvents.collect({arg item, i;
						var startTime, noteLength, pb;
						// midiPitchBendEvents format: [trackNumber, absTime, type, channel, val1, val2]
						// TX Controller Event format: [timeBeats, normalisedVal]
						[item[1] * divisionRecip, 0.5 + (item[4] / (2**14))];
					});
					if (midiTrackName.containsi("[pb]"), {
						nameString = midiTrackName;
					}, {
						nameString = midiTrackName ++ " [pb]";
					});
					this.addCtrlTrackFromMidi(nameString,
						arrMidiEvents[0][3], arrMidiEvents[0][4], 1, arrEvents);
				});
			});
			// create controller tracks
			midiControllerEvents = newMIDIFile.controllerEvents(nil, nil, midiTrackNum);
			if (midiControllerEvents.size > 0, {
				controllerGroups = midiControllerEvents.sort({arg a, b; a[3] < b[3]}).separate({arg a, b; a[3] != b[3]});
				controllerGroups.do({arg arrMidiEvents, i;
					var ccGroups, nameString;
					ccGroups = arrMidiEvents.sort({arg a, b; a[4] < b[4];}).separate({arg a, b; a[4] != b[4]});
					ccGroups.do({arg arrMidiEvents, i;
						arrEvents = arrMidiEvents.collect({arg item, i;
							// midiControllerEvents format: [trackNumber, absTime, type, channel, val1, val2]
							// TX Controller Event format: [timeBeats, normalisedVal]
							[item[1] * divisionRecip, item[5] * recip127];
						});
					if (midiTrackName.containsi("[cc" ++ arrMidiEvents[0][4].asString ++ "]"), {
						nameString = midiTrackName;
					}, {
						nameString = midiTrackName ++ " [cc" ++ arrMidiEvents[0][4].asString ++ "]";
					});
						this.addCtrlTrackFromMidi(nameString,
							arrMidiEvents[0][3], arrMidiEvents[0][4], 0, arrEvents);
					});
				});
			});

		}); // end of numMidiTracks.do
	}

	addCtrlTrackFromMidi {arg name, midiChannel, midiCC, midiPitchBend, controllerEvents;
		var holdTrack;
		holdTrack = parentModule.addTrack('Controller');
		holdTrack.name = name;
		holdTrack.outputMidiChannel = midiChannel;
		holdTrack.outputMidiControllerNo = midiCC;
		holdTrack.outputMidiPitchBend = midiPitchBend;
		// add edit events
		holdTrack.arrEditControllerVals = controllerEvents;
		// save history with edits
		this.doWithHistory(holdTrack, {
			// merge
			this.mergeEditControllerVals(holdTrack);
		});
	}

	addNoteTrackFromMidi {arg name, midiChannel, noteEvents;
		var holdTrack;
		holdTrack = parentModule.addTrack('Note');
		holdTrack.name = name;
		holdTrack.outputMidiChannel = midiChannel;
		// save history with edits
		this.doWithHistory(holdTrack, {
			// merge
			this.mergeNewNoteOnOffs(holdTrack, noteEvents);
		});
	}

	saveMidiFile {
		var selectedTracks = parentModule.arrTracks.select({arg track, i; track.selected == 1;});
		// Show error screen if no selected tracks, else save
		if (selectedTracks.isEmpty, {
			TXInfoScreen.new(
				"ERROR: no tracks have been selected - unable to save MIDI file");
		}, {
			Dialog.savePanel({ arg path;
				this.buildWriteMidiFile(path, selectedTracks);
			});
		});
	}

	buildWriteMidiFile {arg path, selectedTracks;
		var newMIDIFile, division, tempoTrack, initTimeSigString, arrProcessTracks;
		var bpmControlSpec = ControlSpec(1, 999);
		var initBpm = 120; // default
		// create empty file
		newMIDIFile = SimpleMIDIFile(path);
		division = newMIDIFile.division;	// division: number of ticks per quarter note
		newMIDIFile.init1(selectedTracks.size + 1, initBpm, initTimeSigString);	// init for type 1 (multitrack)
		initTimeSigString = parentModule.getSynthArgSpec("beatsPerBar").asString ++ "/4";
		// add midi tempo events
		// use the first tempo track, if present, where name starts with "tempo track" (case insensitive)
		arrProcessTracks = selectedTracks.copy;
		tempoTrack = selectedTracks.select({arg item, i; item.name.icontainsStringAt(0, "tempo track")}).first;
		if (tempoTrack.notNil, {
			tempoTrack.arrControllerVals.do({arg item, i;
				// TX Controller Event format: [timeBeats, normalisedVal]
				newMIDIFile.addTempo(bpmControlSpec.map(item[1]), item[0] * division);
			});
			arrProcessTracks.remove(tempoTrack);
		});

		// add midi tracks
		arrProcessTracks.do({arg holdTrack, trackNum;
			var channel, controllerNo, holdPb, pb1, pb2;
			channel = holdTrack.outputMidiChannel;
			// name
			if (holdTrack.name != "", {
				newMIDIFile.setTrackName(holdTrack.name, trackNum + 1);
			});
			// note track
			if (holdTrack.trackType == 'Note', {
				holdTrack.arrNotes.do({arg item, i;
					// TX Note Event format: [startTime, noteNum, vel, noteLength, endTime]
					// addNote { |noteNumber, velo, startTime, dur, upVelo, channel, track
					newMIDIFile.addNote(item[1], item[2], item[0] * division, item[3] * division, item[2], channel, trackNum + 1);
				});
			});
			// controller track
			if (holdTrack.trackType == 'Controller', {
				if (holdTrack.outputMidiPitchBend == 1, {
					// pitchbend
					holdTrack.arrControllerVals.do({arg item, i;
						// TX Controller Event format: [timeBeats, normalisedVal]
						// addMIDITypeEvent(type = \cc, channel = 0, args, absTime = 0, track)
						holdPb = (item[1] * 16383).round(1) / 128;
						pb1 = holdPb.frac * 128;
						pb2 = holdPb.floor;
						newMIDIFile.addMIDITypeEvent(\pitchBend, channel, [pb1, pb2], item[0] * division, trackNum + 1);
					});
				}, {
					//cc
					controllerNo = holdTrack.outputMidiControllerNo;
					holdTrack.arrControllerVals.do({arg item, i;
						// TX Controller Event format: [timeBeats, normalisedVal]
						// addCC { |cc = 7, val = 127, startTime = 0, channel = 0, track
						newMIDIFile.addCC(controllerNo, item[1] * 127, item[0] * division, channel, trackNum + 1);
					});
				});
			});
		});
		// final prep
		newMIDIFile.adjustEndOfTrack;
		newMIDIFile.correctTempoEvents;
		// write new file
		newMIDIFile.write;
	}

}
