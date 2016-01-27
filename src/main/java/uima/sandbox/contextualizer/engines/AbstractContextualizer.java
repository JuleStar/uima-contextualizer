package uima.sandbox.contextualizer.engines;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.contextualizer.models.Context;


public abstract class AbstractContextualizer extends JCasAnnotator_ImplBase {
	
	public static final String SCOPE = "Scope";
	@ConfigurationParameter(name = SCOPE, mandatory = true)
	private int scope;
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			Context context = new Context(cas,this.scope);
			List<List<Annotation>> lists = context.make();
			for (List<Annotation> list : lists) {
				if (!list.isEmpty()) {
					Annotation annotation = list.get(0);
					Annotation[] annotations = new Annotation[list.size() - 1];
					for (int i = 1; i < list.size(); i++) {
						annotations[i - 1] = list.get(i);
					}
					this.annotate(cas, annotation, annotations);
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	protected abstract void annotate(JCas cas,Annotation annotation, Annotation[] annotations); 
	
}
