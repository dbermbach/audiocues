/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.metrics.analyzers.QuadraticMetricEventAnalyzer;
import de.tuberlin.ise.dbe.metrics.consumers.MockMonitorConsumer;
import de.tuberlin.ise.dbe.midi.instruments.Instruments;
import de.tuberlin.ise.monitoring.generators.Distribution;

/**
 * @author Dave
 *
 */
public class AudioCueStarter {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		MetricMonitor metricMonitor = new MetricMonitor();
//		metricMonitor.addAnalyzer(0, new LinearMetricEventAnalyzer(0, 100));
		metricMonitor.addAnalyzer(0, new QuadraticMetricEventAnalyzer(0, 100));
//		metricMonitor.addMetricConsumer(new BinaryMetricConsumer(5000,
//				true, metricMonitor));
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
