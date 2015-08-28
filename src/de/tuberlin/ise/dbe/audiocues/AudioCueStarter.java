/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import de.tuberlin.ise.dbe.metrics.DummyConsumer;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.metrics.analyzers.LinearMetricEventAnalyzer;
import de.tuberlin.ise.dbe.midi.instruments.Instruments;

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
		metricMonitor.addAnalyzer(0, new LinearMetricEventAnalyzer(20, 100));
		metricMonitor.addMetricConsumer(new DummyConsumer(3000,metricMonitor));
		metricMonitor.start();
		
		MusicGraphNode startNode = MusicGraphBuilder.buildGraph();
		AudioCuePlayer player = new AudioCuePlayer(4, startNode, 144, metricMonitor);
		player.changeInstrument(0, Instruments.Pianos.Acoustic_Grand_Piano);
		new Thread(player).start();

	}

	
	
	
}
