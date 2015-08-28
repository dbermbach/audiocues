/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues.debug;

import de.tuberlin.ise.dbe.audiocues.AudioCuePlayer;
import de.tuberlin.ise.dbe.audiocues.MusicGraphNode;
import de.tuberlin.ise.dbe.audiocues.SampleMusicGraphBuilder;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.metrics.analyzers.LinearMetricEventAnalyzer;
import de.tuberlin.ise.dbe.metrics.consumers.MockMonitorConsumer;
import de.tuberlin.ise.dbe.midi.instruments.Instruments;
import de.tuberlin.ise.monitoring.generators.Distribution;

/**
 * @author Dave
 *
 */
public class SimpleMusicGraphPlayer {

	public static final boolean dissonanceOn = false;

	public static void main(String[] args) throws Exception {
		MetricMonitor metricMonitor = new MetricMonitor();
		metricMonitor.addAnalyzer(0, new LinearMetricEventAnalyzer(0, 100));
//		metricMonitor.addMetricConsumer(new BinaryMetricConsumer(5000,
//				dissonanceOn, metricMonitor));
		metricMonitor.addMetricConsumer(new MockMonitorConsumer(1000, Distribution.SIM_CHAINED, metricMonitor));
		metricMonitor.start();

		MusicGraphNode startNode = SampleMusicGraphBuilder.buildGraph();
		AudioCuePlayer player = new AudioCuePlayer(2, startNode, 138,
				metricMonitor);
		player.changeInstrument(0, Instruments.Synth.Pad_2_warm);
		player.changeInstrument(1, Instruments.Synth.Lead_8_bass);
		player.changeInstrument(2, Instruments.Guitars.Electric_Guitar_muted);
		player.changeInstrument(3, Instruments.Pianos.Acoustic_Grand_Piano);
		new Thread(player).start();
	}

}
