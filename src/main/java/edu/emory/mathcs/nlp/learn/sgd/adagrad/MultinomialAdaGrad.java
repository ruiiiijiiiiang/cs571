/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.learn.sgd.adagrad;

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultinomialAdaGrad extends AdaGrad
{
	public MultinomialAdaGrad(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}

	@Override
	protected void updateWeightVector(Instance instance, int steps)
	{
		Vector x = instance.getVector();
		int yp = instance.getLabel(), yn = bestMultinomiaLabelHinge(instance);
		
		if (yp != yn)
		{
			updateDiagonals(x, yp);
			updateDiagonals(x, yn);
			
			weight_vector.update(x, yp, (i,j) ->  getGradient(i,j));
			weight_vector.update(x, yn, (i,j) -> -getGradient(i,j));
			
			if (isAveraged())
			{
				average_vector.update(x, yp, (i,j) ->  getGradient(i,j) * steps);
				average_vector.update(x, yn, (i,j) -> -getGradient(i,j) * steps);
			}
		}
	}
}