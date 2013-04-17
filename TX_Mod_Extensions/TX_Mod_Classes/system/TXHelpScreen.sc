// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXHelpScreen {
	classvar v,w, keepOnTop = false;

	*open{ arg winColour=Color(0.6,0.8,0.8), inLeft=50, inTop=500;
		var button, arrInfoLinesHeight, b,c,d,e,f,g, t, u;

		{
			if (w.isNil, {
				winColour = winColour ;
				w = Window("TX Modular System - Help", Rect(inLeft, inTop, 600, 600));
				w.view.decorator = FlowLayout(w.view.bounds);
				w.view.background = winColour;
				w.alwaysOnTop = keepOnTop;
				w.front;
				w.onClose = {w = nil};

				e = Button(w, Rect(0,0, 25, 24));
				e.states = [["< ", Color.black, Color.white]];
				e.action = {v.back };

				f = Button(w, Rect(0,0, 25, 24));
				f.states = [["> ", Color.black, Color.white]];
				f.action = {v.forward};

				w.view.decorator.shift(10,0);

				b = Button(w, Rect(0,0, 75, 24));
				b.states = [["Index ", Color.black, Color.white]];
				b.action = {this.goToIndex;};

				w.view.decorator.shift(10,0);

				u = StaticText(w, Rect(0,0, 50, 24));
				u.string = "Find:";
				u.stringColor_(Color.green(0.5));
				u.background_(Color.white);

				t = TextField(w, Rect(0,0, 125, 24));
				t.action = {v.findText(t.string)};

				b = Button(w, Rect(0,0, 20, 24));
				b.states = [["x ", Color.green(0.5), Color.white]];
				b.action = {t.string = "";};

				b = Button(w, Rect(0,0, 40, 24));
				b.states = [["Next ", Color.green(0.5), Color.white]];
				b.action = {v.findText(t.string)};

				b = Button(w, Rect(0,0, 40, 24));
				b.states = [["Prev ", Color.green(0.5), Color.white]];
				b.action = {v.findText(t.string, true)};

				// c = Button(w, Rect(0,0, 75, 24));
				// c.states = [["Other ", Color.black, Color.white]];
				// c.action = {v.url = "file:///Users/paul/TESTING/html_convert_tests/TX_Mod_Help 086/Modules/TX_Action Sequencer.html";};

				w.view.decorator.nextLine;

				g = StaticText(w, Rect(0,0, 590, 30));
				g.stringColor_(Color.grey(0.2));
				g.background_(Color.white);
				g.font_( Font("Helvetica", 14));
				g.resize_(2);
				g.align_(\center);

				w.view.decorator.nextLine;

				v = WebView(w, Rect(0,0, 590, 530));
				v.onLoadFailed = {arg view;("Failed to load URL: " ++ view.url.asString).postln;};
				v.onLoadFinished = {arg view;
					var holdString = view.url.asString;
					holdString = holdString.asPathName.fileNameWithoutExtension;
					holdString = holdString.replace("TX_0", "").replace("TX_", "");
					holdString = holdString.replace("_", " ");
					g.string = holdString;
				};
				//v.onLinkActivated = {arg view, url; g.string = url.asString; v.url = url};
				v.resize_(5);

				this.goToIndex;

				}, {
					w.front;
			});
		}.defer;
	}

	*goToIndex {arg indexString = "/TX_Mod_Help/TX_0 TX Modular Help.html";
		{v.url = this.class.filenameSymbol.asString.dirname ++ indexString;}.defer;
	}

	*openModule {arg moduleName;
		var holdString;
		{
			this.open;
			holdString = this.class.filenameSymbol.asString.dirname ++ "/TX_Mod_Help/Modules/" ++ moduleName ++ ".html";
			("Opening Help File: " ++ holdString).postln;
			v.url = holdString;
		}.defer;
	}

	*close {		//	close window
		// OLD if (w.isClosed.not, {w.close;});
		// NEW:
		if (w.isClosed.not, {{w.close; w = nil;}.defer;});
	}


}
